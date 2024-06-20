package com.example.housekeeper.data.product

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM product")
    fun getAll(): Flow<List<DataProduct>>

    @Query("SELECT * FROM product WHERE name = :name")
    suspend fun getOrNull(name: String): DataProduct?

    @Insert
    suspend fun insert(product: DataProduct)
}