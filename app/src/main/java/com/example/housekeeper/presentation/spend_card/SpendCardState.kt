package com.example.housekeeper.presentation.spend_card

import androidx.compose.ui.text.input.TextFieldValue
import com.example.housekeeper.domain.Currency
import com.example.housekeeper.domain.product.AmountType
import com.example.housekeeper.domain.product.Product
import com.example.housekeeper.domain.shop.Shop
import com.example.housekeeper.presentation.ImmutableList

data class SpendCardState(
    val priceFieldValue: TextFieldValue,
    val currency: Currency,
    val availableCurrencies: ImmutableList<Currency>,
    val amountFieldValue: TextFieldValue,
    val amountType: AmountType,
    val isProductDropdownEnabled: Boolean,
    val product: Product?,
    val availableProducts: ImmutableList<Product>,
    val isShopDropdownEnabled: Boolean,
    val shop: Shop?,
    val availableShops: ImmutableList<Shop>,
)