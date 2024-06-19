package com.example.housekeeper.presentation.spend_card

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.example.housekeeper.domain.Currency
import com.example.housekeeper.domain.product.Product
import com.example.housekeeper.presentation.NavManager
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

class SpendCardViewModel(
    navManager: NavManager,
) : ContainerHost<SpendCardState, SpendCardSideEffect>, ViewModel() {
    override val container = container<SpendCardState, SpendCardSideEffect>(initState())

    fun <T> accept(action: (T) -> SpendCardUIEvent): (T) -> Unit = { input ->
        accept(action(input))
    }

    fun accept(action: () -> SpendCardUIEvent): () -> Unit = {
        accept(action())
    }

    fun accept(event: SpendCardUIEvent) {
        when(event) {
            is SpendCardUIEvent.PriceChanged -> setPrice(event.newValue)
            is SpendCardUIEvent.CurrencyChanged -> setCurrency(event.newValue)
            is SpendCardUIEvent.ProductChanged -> setProduct(event.newValue)
            SpendCardUIEvent.AddProductClick -> addProductClick()
            is SpendCardUIEvent.AddProduct -> addProduct(event.name)
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

    private fun setProduct(product: Product) = intent {
        reduce {
            state.copy(product = product)
        }
    }

    private fun addProductClick() = intent {
        postSideEffect(SpendCardSideEffect.ShowAddProductDialog)
    }

    private fun addProduct(name: String) = productIntentWithIndicator {

    }

    private fun productIntentWithIndicator(
        action: suspend SimpleSyntax<SpendCardState, SpendCardSideEffect>.() -> Unit
    ) = intent {
        postSideEffect(SpendCardSideEffect.ChangeProductLoadingVisibility(true))
        try {
            action()
        } finally {
            postSideEffect(SpendCardSideEffect.ChangeProductLoadingVisibility(false))
        }
    }
}

private fun initState() = SpendCardState(
    priceFieldValue = TextFieldValue(""),
    currency = Currency.Ruble,
    availableCurrencies = listOf(Currency.Ruble, Currency.Dollar, Currency.Euro),
    isProductDropdownEnabled = false,
    product = null,
    availableProducts = emptyList(),
)