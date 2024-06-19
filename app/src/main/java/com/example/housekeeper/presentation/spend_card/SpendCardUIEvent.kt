package com.example.housekeeper.presentation.spend_card

import androidx.compose.ui.text.input.TextFieldValue
import com.example.housekeeper.domain.Currency
import com.example.housekeeper.domain.product.Product
import com.example.housekeeper.presentation.UIEvent

sealed interface SpendCardUIEvent : UIEvent {
    class PriceChanged(val newValue: TextFieldValue) : SpendCardUIEvent
    class CurrencyChanged(val newValue: Currency) : SpendCardUIEvent
    class ProductChanged(val newValue: Product) : SpendCardUIEvent
    data object AddProductClick : SpendCardUIEvent
    class AddProduct(val name: String) : SpendCardUIEvent
}