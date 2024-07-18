package com.example.housekeeper.presentation.spend_card

import androidx.compose.ui.text.input.TextFieldValue
import com.example.housekeeper.domain.Currency
import com.example.housekeeper.domain.product.AmountType
import com.example.housekeeper.domain.product.Product
import com.example.housekeeper.domain.shop.Shop
import com.example.housekeeper.presentation.UIEvent

sealed interface SpendCardUIEvent : UIEvent {
    data class PriceChanged(val newValue: TextFieldValue) : SpendCardUIEvent
    data class CurrencyChanged(val newValue: Currency) : SpendCardUIEvent
    data class AmountChanged(val newValue: TextFieldValue) : SpendCardUIEvent
    data class AmountTypeChanged(val newValue: AmountType) : SpendCardUIEvent
    data class ProductChanged(val newValue: Product?) : SpendCardUIEvent
    data object AddProductClick : SpendCardUIEvent
    data class AddProduct(val name: String) : SpendCardUIEvent
    data class DeleteProductClick(val product: Product) : SpendCardUIEvent
    data class ShopChanged(val newValue: Shop?) : SpendCardUIEvent
    data object AddShopClick : SpendCardUIEvent
    data class AddShop(val name: String) : SpendCardUIEvent
    data class DeleteShopClick(val shop: Shop) : SpendCardUIEvent
    data class DateChanged(val newValue: Long) : SpendCardUIEvent
    data class CommentChanged(val newValue: TextFieldValue) : SpendCardUIEvent
    data object SaveClick : SpendCardUIEvent
}