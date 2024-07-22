package com.example.housekeeper.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.housekeeper.data.product.DataProduct
import com.example.housekeeper.data.product.ProductDao
import com.example.housekeeper.data.shop.DataShop
import com.example.housekeeper.data.shop.ShopDao
import com.example.housekeeper.data.spend.DataSpend
import com.example.housekeeper.data.spend.SpendDao

@Database(
    entities = [
        DataProduct::class,
        DataShop::class,
        DataSpend::class,
    ],
    version = 2,
    exportSchema = true,
    autoMigrations = [
    ],
)
@TypeConverters(
    CurrencyConverter::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun shopDao(): ShopDao
    abstract fun spendDao(): SpendDao
}