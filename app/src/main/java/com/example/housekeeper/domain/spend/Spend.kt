package com.example.housekeeper.domain.spend

import com.example.housekeeper.domain.Currency
import com.example.housekeeper.domain.product.AmountType
import com.example.housekeeper.domain.product.Product
import com.example.housekeeper.domain.shop.Shop

data class Spend(
    val id: Int,
    val dateMillis: Long,
    val price: Float,
    val currency: Currency,
    val amount: Int,
    val amountType: AmountType,
    val product: Product,
    val shop: Shop?,
    val comment: String,
)