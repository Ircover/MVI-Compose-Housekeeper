package com.example.housekeeper.presentation.spend_card

import com.example.housekeeper.domain.Currency
import com.example.housekeeper.domain.Product
import com.example.housekeeper.presentation.UIEvent

sealed interface SpendCardUIEvent : UIEvent {
    class PriceChanged(val newValue: String) : SpendCardUIEvent
    class CurrencyChanged(val newValue: Currency) : SpendCardUIEvent
    class ProductChanged(val newValue: Product) : SpendCardUIEvent
}