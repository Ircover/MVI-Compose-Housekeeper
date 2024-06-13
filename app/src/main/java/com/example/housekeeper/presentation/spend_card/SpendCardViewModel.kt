package com.example.housekeeper.presentation.spend_card

import androidx.lifecycle.ViewModel
import com.example.housekeeper.domain.Currency
import com.example.housekeeper.domain.Product
import com.example.housekeeper.presentation.NavManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SpendCardViewModel(
    navManager: NavManager,
) : ViewModel() {
    private val _state = MutableStateFlow(initState())
    val state = _state.asStateFlow()

    fun accept(event: SpendCardUIEvent) {
        when(event) {
            is SpendCardUIEvent.PriceChanged -> setPrice(event.newValue)
            is SpendCardUIEvent.CurrencyChanged -> setCurrency(event.newValue)
            is SpendCardUIEvent.ProductChanged -> setProduct(event.newValue)
        }
    }

    private fun setPrice(newPrice: String) {
        if (newPrice.isEmpty() || newPrice.toFloatOrNull() != null) {
            val isFloatingPartTooLong =
                (newPrice.split(".", ",").getOrNull(1)?.length ?: 0) > 2
            if (!isFloatingPartTooLong) {
                _state.update {
                    it.copy(price = newPrice)
                }
            }
        }
    }

    private fun setCurrency(currency: Currency) {
        _state.update {
            it.copy(currency = currency)
        }
    }

    private fun setProduct(product: Product) {
        _state.update {
            it.copy(product = product)
        }
    }
}

private fun initState() = SpendCardState(
    price = "",
    currency = Currency.Ruble,
    availableCurrencies = listOf(Currency.Ruble, Currency.Dollar, Currency.Euro),
    isProductDropdownEnabled = false,
    product = null,
    availableProducts = emptyList(),
)