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
import com.example.housekeeper.presentation.NavManager
import com.example.housekeeper.presentation.UserMessage
import com.example.housekeeper.presentation.UserMessageLevel
import com.example.housekeeper.presentation.UserMessageShowDuration
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
    navManager: NavManager,
) : ContainerHost<SpendCardState, SpendCardSideEffect>, ViewModel() {
    override val container = container<SpendCardState, SpendCardSideEffect>(initState())

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
                    SpendCardSideEffect.ShowMessage(
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
                    SpendCardSideEffect.ShowMessage(
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

    private fun addProductIntentWithIndicator(
        action: suspend SimpleSyntax<SpendCardState, SpendCardSideEffect>.() -> Unit
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
}

private fun initState() = SpendCardState(
    priceFieldValue = TextFieldValue(""),
    currency = Currency.Ruble,
    availableCurrencies = listOf(Currency.Ruble, Currency.Dollar, Currency.Euro).toImmutable(),
    amountFieldValue = TextFieldValue(),
    amountType = AmountType.Count,
    isProductDropdownEnabled = false,
    product = null,
    availableProducts = emptyList<Product>().toImmutable(),
)