package com.example.housekeeper.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.housekeeper.data.product.DataProduct
import com.example.housekeeper.data.product.ProductDao

@Database(entities = [DataProduct::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}