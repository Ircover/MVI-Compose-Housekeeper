package com.example.housekeeper.domain.product.repository

interface ProductRepository {
    suspend fun hasProduct(name: String): Boolean
    suspend fun addProduct(name: String): Result<Unit>
}

class ProductRepositoryImpl: ProductRepository {
    override suspend fun hasProduct(name: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun addProduct(name: String): Result<Unit> {
        TODO("Not yet implemented")
    }
}