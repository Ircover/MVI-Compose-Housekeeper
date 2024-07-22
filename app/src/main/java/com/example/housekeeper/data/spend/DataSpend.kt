package com.example.housekeeper.data.spend

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.housekeeper.data.product.DataProduct
import com.example.housekeeper.data.shop.DataShop
import com.example.housekeeper.domain.Currency
import com.example.housekeeper.domain.product.AmountType

@Entity(tableName = "spend")
data class DataSpend(
    @PrimaryKey val id: Long,
    val dateMillis: Long,
    val price: Float,
    val currency: Currency,
    val amount: Int,
    val amountType: AmountType,
    @Embedded(prefix = "product_") val product: DataProduct,
    @Embedded(prefix = "shop_") val shop: DataShop?,
    val comment: String,
)