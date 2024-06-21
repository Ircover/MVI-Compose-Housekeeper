package com.example.housekeeper.domain.product.repository

import com.example.housekeeper.domain.product.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun hasProduct(name: String): Boolean
    suspend fun addProduct(name: String): Result<Product>
    fun getProducts(): Flow<List<Product>>
}