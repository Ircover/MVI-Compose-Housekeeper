package com.example.housekeeper.data

import androidx.room.TypeConverter
import com.example.housekeeper.domain.Currency

class CurrencyConverter {
    @TypeConverter
    fun toCurrency(sign: String?): Currency? =
        sign?.let {
            currenciesMap[sign]
        }

    @TypeConverter
    fun toString(currency: Currency?): String? = currency?.sign?.toString()

    private companion object {
        val currenciesMap = Currency.All.associateBy { it.sign.toString() }
    }
}