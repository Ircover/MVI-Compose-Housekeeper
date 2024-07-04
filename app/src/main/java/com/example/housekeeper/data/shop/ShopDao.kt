package com.example.housekeeper.data.shop

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ShopDao {
    @Query("SELECT * FROM shop")
    fun getAll(): Flow<List<DataShop>>

    @Query("SELECT * FROM shop WHERE name = :name")
    suspend fun getOrNull(name: String): DataShop?

    @Insert
    suspend fun insert(product: DataShop)
}