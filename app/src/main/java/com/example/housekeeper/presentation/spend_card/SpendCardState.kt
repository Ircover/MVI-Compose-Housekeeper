package com.example.housekeeper.presentation.spend_card

import androidx.compose.ui.text.input.TextFieldValue
import com.example.housekeeper.domain.Currency
import com.example.housekeeper.domain.product.Product

data class SpendCardState(
    val priceFieldValue: TextFieldValue,
    val currency: Currency,
    val availableCurrencies: List<Currency>,
    val isProductDropdownEnabled: Boolean,
    val product: Product?,
    val availableProducts: List<Product>,
)