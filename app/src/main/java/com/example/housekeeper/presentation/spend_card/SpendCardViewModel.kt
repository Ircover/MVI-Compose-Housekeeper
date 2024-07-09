package com.example.housekeeper.presentation.spend_card

import android.util.Log
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.housekeeper.R
import com.example.housekeeper.domain.Currency
import com.example.housekeeper.domain.product.AmountType
import com.example.housekeeper.domain.product.Product
import com.example.housekeeper.domain.product.usecase.AddProductResult
import com.example.housekeeper.domain.product.usecase.AddProductUsecase
import com.example.housekeeper.domain.product.usecase.GetProductsUsecase
import com.example.housekeeper.domain.shop.Shop
import com.example.housekeeper.domain.shop.usecase.AddShopResult
import com.example.housekeeper.domain.shop.usecase.AddShopUsecase
import com.example.housekeeper.domain.shop.usecase.GetShopsUsecase
import com.example.housekeeper.presentation.DateManager
import com.example.housekeeper.presentation.NavManager
import com.example.housekeeper.presentation.UserMessage
import com.example.housekeeper.presentation.UserMessageLevel
import com.example.housekeeper.presentation.UserMessageShowDuration
import com.example.housekeeper.presentation.emptyImmutableList
import com.example.housekeeper.presentation.toImmutable
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

class SpendCardViewModel(
    private val addProductUsecase: AddProductUsecase,
    getProductsUsecase: GetProductsUsecase,
    private val addShopUsecase: AddShopUsecase,
    getShopsUsecase: GetShopsUsecase,
    navManager: NavManager,
    private val dateManager: DateManager,
) : ContainerHost<SpendCardState, SpendCardSideEffect>, ViewModel() {
    override val container = container<SpendCardState, SpendCardSideEffect>(
        dateManager.now().let { now ->
            initState(now, dateManager.formatForUser(now))
        }
    )

    init {
        viewModelScope.launch {
            getProductsUsecase().collect { products ->
                intent {
                    reduce {
                        state.copy(
                            availableProducts = products.toImmutable(),
                            isProductDropdownEnabled = products.isNotEmpty()
                        )
                    }
                }
            }
        }
        viewModelScope.launch {
            getShopsUsecase().collect { shops ->
                intent {
                    reduce {
                        state.copy(
                            availableShops = shops.toImmutable(),
                            isShopDropdownEnabled = shops.isNotEmpty()
                        )
                    }
                }
            }
        }
    }

    inline fun <T> accept(crossinline action: (T) -> SpendCardUIEvent): (T) -> Unit = { input ->
        accept(action(input))
    }

    inline fun accept(crossinline action: () -> SpendCardUIEvent): () -> Unit = {
        accept(action())
    }

    fun accept(event: SpendCardUIEvent) {
        when(event) {
            is SpendCardUIEvent.PriceChanged -> setPrice(event.newValue)
            is SpendCardUIEvent.CurrencyChanged -> setCurrency(event.newValue)
            is SpendCardUIEvent.AmountChanged -> setAmount(event.newValue)
            is SpendCardUIEvent.AmountTypeChanged -> setAmountType(event.newValue)
            is SpendCardUIEvent.ProductChanged -> setProduct(event.newValue)
            SpendCardUIEvent.AddProductClick -> addProductClick()
            is SpendCardUIEvent.AddProduct -> addProduct(event.name)
            is SpendCardUIEvent.DeleteProductClick -> deleteProduct(event.product)
            is SpendCardUIEvent.AddShop -> addShop(event.name)
            SpendCardUIEvent.AddShopClick -> addShopClick()
            is SpendCardUIEvent.DeleteShopClick -> deleteShop(event.shop)
            is SpendCardUIEvent.ShopChanged -> setShop(event.newValue)
            is SpendCardUIEvent.DateChanged -> setDate(event.newValue)
        }
    }

    private fun setPrice(newPrice: TextFieldValue) = intent {
        val newText = newPrice.text
        if (newText.isEmpty() || newText.toFloatOrNull() != null) {
            val isFloatingPartTooLong =
                (newText.split(".", ",").getOrNull(1)?.length ?: 0) > 2
            if (!isFloatingPartTooLong) {
                reduce {
                    state.copy(priceFieldValue = newPrice)
                }
            }
        }
    }

    private fun setCurrency(currency: Currency) = intent {
        reduce {
            state.copy(currency = currency)
        }
    }

    private fun setAmount(newAmount: TextFieldValue) = intent {
        if (newAmount.text.all { it.isDigit() }) {
            reduce {
                state.copy(amountFieldValue = newAmount)
            }
        }
    }

    private fun setAmountType(newType: AmountType) = intent {
        if (state.amountType != newType) {
            reduce {
                state.copy(
                    amountType = newType,
                    amountFieldValue = TextFieldValue("")
                )
            }
        }
    }

    private fun setProduct(product: Product?) = intent {
        reduce {
            state.copy(product = product)
        }
    }

    private fun addProductClick() = intent {
        postSideEffect(SpendCardSideEffect.ShowAddProductDialog)
    }

    private fun addProduct(name: String) = addProductIntentWithIndicator {
        val result = addProductUsecase(name)
        when (result) {
            AddProductResult.ProductAlreadyExists -> {
                postSideEffect(
                    SpendCardSideEffect.ShowProductDialogMessage(
                        UserMessage(
                            R.string.error_product_exists,
                            UserMessageLevel.Info,
                            UserMessageShowDuration.Short,
                        )
                    )
                )
            }
            is AddProductResult.UnknownError -> {
                Log.e("SpendCardViewModel", "add product error: ${result.t}")
                postSideEffect(
                    SpendCardSideEffect.ShowProductDialogMessage(
                        UserMessage(
                            R.string.error_unknown,
                            UserMessageLevel.Error,
                            UserMessageShowDuration.Long,
                        )
                    )
                )
            }
            is AddProductResult.Success -> {
                postSideEffect(SpendCardSideEffect.HideAddProductDialog)
                reduce {
                    state.copy(product = result.product)
                }
            }
        }
    }

    private inline fun addProductIntentWithIndicator(
        crossinline action: suspend SimpleSyntax<SpendCardState, SpendCardSideEffect>.() -> Unit
    ) = intent {
        postSideEffect(SpendCardSideEffect.ChangeAddProductDialogLoading(true))
        try {
            action()
        } finally {
            postSideEffect(SpendCardSideEffect.ChangeAddProductDialogLoading(false))
        }
    }

    private fun deleteProduct(product: Product) = intent {
        postSideEffect(
            SpendCardSideEffect.ShowMessage(
                UserMessage(
                    R.string.not_implemented,
                    UserMessageLevel.Info,
                    UserMessageShowDuration.Short,
                )
            )
        )
    }

    private fun setShop(shop: Shop?) = intent {
        reduce {
            state.copy(shop = shop)
        }
    }

    private fun addShopClick() = intent {
        postSideEffect(SpendCardSideEffect.ShowAddShopDialog)
    }

    private fun addShop(name: String) = addShopIntentWithIndicator {
        val result = addShopUsecase(name)
        when (result) {
            AddShopResult.ShopAlreadyExists -> {
                postSideEffect(
                    SpendCardSideEffect.ShowShopDialogMessage(
                        UserMessage(
                            R.string.error_shop_exists,
                            UserMessageLevel.Info,
                            UserMessageShowDuration.Short,
                        )
                    )
                )
            }
            is AddShopResult.UnknownError -> {
                Log.e("SpendCardViewModel", "add shop error: ${result.t}")
                postSideEffect(
                    SpendCardSideEffect.ShowShopDialogMessage(
                        UserMessage(
                            R.string.error_unknown,
                            UserMessageLevel.Error,
                            UserMessageShowDuration.Long,
                        )
                    )
                )
            }
            is AddShopResult.Success -> {
                postSideEffect(SpendCardSideEffect.HideAddShopDialog)
                reduce {
                    state.copy(shop = result.shop)
                }
            }
        }
    }

    private inline fun addShopIntentWithIndicator(
        crossinline action: suspend SimpleSyntax<SpendCardState, SpendCardSideEffect>.() -> Unit
    ) = intent {
        postSideEffect(SpendCardSideEffect.ChangeAddShopDialogLoading(true))
        try {
            action()
        } finally {
            postSideEffect(SpendCardSideEffect.ChangeAddShopDialogLoading(false))
        }
    }

    private fun deleteShop(shop: Shop) = intent {
        postSideEffect(
            SpendCardSideEffect.ShowMessage(
                UserMessage(
                    R.string.not_implemented,
                    UserMessageLevel.Info,
                    UserMessageShowDuration.Short,
                )
            )
        )
    }

    private fun setDate(dateMillis: Long) = intent {
        reduce {
            state.copy(
                dateMillis = dateMillis,
                dateString = dateManager.formatForUser(dateMillis),
            )
        }
    }
}

private fun initState(
    dateMillis: Long,
    dateString: String,
) = SpendCardState(
    dateMillis = dateMillis,
    dateString = dateString,
    priceFieldValue = TextFieldValue(""),
    currency = Currency.Ruble,
    availableCurrencies = listOf(Currency.Ruble, Currency.Dollar, Currency.Euro).toImmutable(),
    amountFieldValue = TextFieldValue("1"),
    amountType = AmountType.Count,
    isProductDropdownEnabled = false,
    product = null,
    availableProducts = emptyImmutableList(),
    isShopDropdownEnabled = false,
    shop = null,
    availableShops = emptyImmutableList()
)