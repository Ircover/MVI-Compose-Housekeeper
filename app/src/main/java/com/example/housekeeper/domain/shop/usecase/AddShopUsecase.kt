package com.example.housekeeper.domain.shop.usecase

import com.example.housekeeper.domain.shop.Shop
import com.example.housekeeper.domain.shop.repository.ShopRepository

class AddShopUsecase(
    private val shopRepository: ShopRepository,
) {
    suspend operator fun invoke(name: String): AddShopResult {
        return if (shopRepository.hasShop(name)) {
            AddShopResult.ShopAlreadyExists
        } else {
            val result = shopRepository.addShop(name)
            if (result.isSuccess) {
                val resultValue = result.getOrNull()
                if (resultValue != null) {
                    AddShopResult.Success(resultValue)
                } else {
                    AddShopResult.UnknownError(null)
                }
            } else {
                AddShopResult.UnknownError(result.exceptionOrNull())
            }
        }
    }
}

sealed interface AddShopResult {
    data class Success(val shop: Shop): AddShopResult
    data object ShopAlreadyExists: AddShopResult
    data class UnknownError(val t: Throwable?): AddShopResult
}