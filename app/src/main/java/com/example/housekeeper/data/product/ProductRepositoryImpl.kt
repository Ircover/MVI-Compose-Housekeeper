package com.example.housekeeper.data.product

import com.example.housekeeper.domain.product.Product
import com.example.housekeeper.domain.product.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductRepositoryImpl(
    private val productDao: ProductDao,
): ProductRepository {
    override suspend fun hasProduct(name: String): Boolean {
        return productDao.getOrNull(name) != null
    }

    override suspend fun addProduct(name: String): Result<Product> {
        return try {
            val dataProduct = DataProduct(name)
            productDao.insert(dataProduct)
            Result.success(dataProduct.toProduct())
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    override fun getProducts(): Flow<List<Product>> =
        productDao.getAll()
            .map { it.map { it.toProduct() } }
}