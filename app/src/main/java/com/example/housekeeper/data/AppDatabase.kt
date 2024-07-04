package com.example.housekeeper.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.housekeeper.data.product.DataProduct
import com.example.housekeeper.data.product.ProductDao
import com.example.housekeeper.data.shop.DataShop
import com.example.housekeeper.data.shop.ShopDao

@Database(
    entities = [DataProduct::class, DataShop::class],
    version = 1,
    exportSchema = true,
    autoMigrations = [
    ],
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun shopDao(): ShopDao
}