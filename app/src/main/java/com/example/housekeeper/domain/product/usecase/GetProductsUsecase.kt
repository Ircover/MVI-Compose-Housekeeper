package com.example.housekeeper.domain.product.usecase

import com.example.housekeeper.domain.product.repository.ProductRepository

class GetProductsUsecase(
    private val productRepository: ProductRepository,
) {
    operator fun invoke() = productRepository.getProducts()
}