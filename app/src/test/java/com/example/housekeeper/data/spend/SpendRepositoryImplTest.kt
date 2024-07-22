package com.example.housekeeper.data.spend

import com.example.housekeeper.data.product.DataProduct
import com.example.housekeeper.data.shop.DataShop
import com.example.housekeeper.domain.Currency
import com.example.housekeeper.domain.product.AmountType
import com.example.housekeeper.domain.product.Product
import com.example.housekeeper.domain.shop.Shop
import com.example.housekeeper.domain.spend.Spend
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.whenever

class SpendRepositoryImplTest {

    private lateinit var sut: SpendRepositoryImpl
    private lateinit var spendDao: SpendDao

    @Before
    fun setup() {
        spendDao = Mockito.mock()
        sut = SpendRepositoryImpl(spendDao)
    }

    @Test
    fun isSpendExists_true(): Unit = runBlocking {
        assertSpendExistence(true)
    }

    @Test
    fun isSpendExists_false(): Unit = runBlocking {
        assertSpendExistence(false)
    }

    @Test
    fun isSpendExists_error(): Unit = runBlocking {
        val id = 1L
        whenever(spendDao.isExist(id)).thenThrow(RuntimeException("test"))

        val result = sut.isSpendExists(id)

        Assert.assertFalse(result)
    }

    @Test
    fun save_default(): Unit = runBlocking {
        val spend = generateSpend()
        val dataSpend = generateDataSpend()
        whenever(spendDao.insert(dataSpend)).thenReturn(1)

        val result = sut.save(spend)

        Assert.assertEquals(Result.success(spend), result)
    }

    @Test
    fun save_error(): Unit = runBlocking {
        val spend = generateSpend()
        val dataSpend = generateDataSpend()
        val error = RuntimeException("test")
        whenever(spendDao.insert(dataSpend)).thenThrow(error)

        val result = sut.save(spend)

        Assert.assertEquals(Result.failure<Spend>(error), result)
    }

    private suspend fun assertSpendExistence(isExists: Boolean) {
        val id = 1L
        whenever(spendDao.isExist(id)).thenReturn(isExists)

        val result = sut.isSpendExists(id)

        Assert.assertEquals(isExists, result)
    }

    private fun generateSpend() = Spend(
        id = 1,
        dateMillis = 1L,
        price = 12F,
        currency = Currency.Ruble,
        amount = 1,
        amountType = AmountType.Count,
        product = Product("test"),
        shop = Shop("test shop"),
        comment = "",
    )

    private fun generateDataSpend() = DataSpend(
        id = 1,
        dateMillis = 1L,
        price = 12F,
        currency = Currency.Ruble,
        amount = 1,
        amountType = AmountType.Count,
        product = DataProduct("test"),
        shop = DataShop("test shop"),
        comment = "",
    )
}