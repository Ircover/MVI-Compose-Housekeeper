package com.example.housekeeper.data.shop

import com.example.housekeeper.domain.shop.Shop
import com.example.housekeeper.domain.shop.repository.ShopRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ShopRepositoryImpl(
    private val shopDao: ShopDao,
): ShopRepository {
    override suspend fun hasShop(name: String): Boolean {
        return shopDao.getOrNull(name) != null
    }

    override suspend fun addShop(name: String): Result<Shop> {
        return try {
            val dataShop = DataShop(name)
            shopDao.insert(dataShop)
            Result.success(dataShop.toShop())
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    override fun getShops(): Flow<List<Shop>> =
        shopDao.getAll()
            .map { it.map { it.toShop() } }
}