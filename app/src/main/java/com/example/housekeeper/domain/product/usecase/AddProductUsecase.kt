package com.example.housekeeper.domain.product.usecase

import com.example.housekeeper.domain.product.Product
import com.example.housekeeper.domain.product.repository.ProductRepository

class AddProductUsecase(
    private val productRepository: ProductRepository,
) {
    suspend operator fun invoke(name: String): AddProductResult {
        return if (productRepository.hasProduct(name)) {
            AddProductResult.ProductAlreadyExists
        } else {
            val result = productRepository.addProduct(name)
            if (result.isSuccess) {
                val resultValue = result.getOrNull()
                if (resultValue != null) {
                    AddProductResult.Success(resultValue)
                } else {
                    AddProductResult.UnknownError(null)
                }
            } else {
                AddProductResult.UnknownError(result.exceptionOrNull())
            }
        }
    }
}

sealed interface AddProductResult {
    data class Success(val product: Product): AddProductResult
    data object ProductAlreadyExists: AddProductResult
    data class UnknownError(val t: Throwable?): AddProductResult
}