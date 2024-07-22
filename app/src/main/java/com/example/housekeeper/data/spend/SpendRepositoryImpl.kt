package com.example.housekeeper.data.spend

import com.example.housekeeper.data.product.toDataProduct
import com.example.housekeeper.data.product.toProduct
import com.example.housekeeper.data.shop.toDataShop
import com.example.housekeeper.data.shop.toShop
import com.example.housekeeper.domain.spend.Spend
import com.example.housekeeper.domain.spend.repository.SpendRepository

class SpendRepositoryImpl(
    private val spendDao: SpendDao,
) : SpendRepository {
    override suspend fun isSpendExists(id: Long) = try {
        spendDao.isExist(id)
    } catch (_: Throwable) {
        false
    }

    override suspend fun save(spend: Spend) = try {
        val dataSpend = spend.toDataSpend()
        val resultId = spendDao.insert(dataSpend)
        Result.success(dataSpend.toSpend(resultId))
    } catch (t: Throwable) {
        Result.failure(t)
    }

    private fun Spend.toDataSpend() = DataSpend(
        id = id,
        dateMillis = dateMillis,
        price = price,
        currency = currency,
        amount = amount,
        amountType = amountType,
        product = product.toDataProduct(),
        shop = shop?.toDataShop(),
        comment = comment,
    )

    private fun DataSpend.toSpend(id: Long = this.id) = Spend(
        id = id,
        dateMillis = dateMillis,
        price = price,
        currency = currency,
        amount = amount,
        amountType = amountType,
        product = product.toProduct(),
        shop = shop?.toShop(),
        comment = comment,
    )
}