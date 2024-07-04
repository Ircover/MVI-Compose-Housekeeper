package com.example.housekeeper.domain.shop.repository

import com.example.housekeeper.domain.shop.Shop
import kotlinx.coroutines.flow.Flow

interface ShopRepository {
    suspend fun hasShop(name: String): Boolean
    suspend fun addShop(name: String): Result<Shop>
    fun getShops(): Flow<List<Shop>>
}