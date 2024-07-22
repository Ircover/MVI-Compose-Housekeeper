package com.example.housekeeper.data.spend

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SpendDao {
    @Query("SELECT * FROM spend")
    fun getAll(): Flow<List<DataSpend>>

    @Query("SELECT EXISTS(SELECT * FROM spend WHERE id = :id)")
    fun isExist(id : Long): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: DataSpend): Long
}