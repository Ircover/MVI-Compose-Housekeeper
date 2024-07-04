package com.example.housekeeper.domain.shop.usecase

import com.example.housekeeper.domain.shop.repository.ShopRepository

class GetShopsUsecase(
    private val shopRepository: ShopRepository,
) {
    operator fun invoke() = shopRepository.getShops()
}