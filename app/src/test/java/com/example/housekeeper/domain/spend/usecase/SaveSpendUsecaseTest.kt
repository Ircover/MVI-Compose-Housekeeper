package com.example.housekeeper.domain.spend.usecase

import com.example.housekeeper.domain.Currency
import com.example.housekeeper.domain.EMPTY_ID
import com.example.housekeeper.domain.product.AmountType
import com.example.housekeeper.domain.product.Product
import com.example.housekeeper.domain.shop.Shop
import com.example.housekeeper.domain.spend.Spend
import com.example.housekeeper.domain.spend.repository.SpendRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class SaveSpendUsecaseTest {

    private lateinit var sut: SaveSpendUsecase
    private lateinit var spendRepository: SpendRepository

    @Before
    fun setup() {
        spendRepository = Mockito.mock()
        sut = SaveSpendUsecase(spendRepository)
    }

    @Test
    fun invoke_newSpend_default(): Unit = runBlocking {
        val spend = generateSpend()
        whenever(spendRepository.save(spend)).thenReturn(Result.success(spend))

        val result = sut(spend)

        assertEquals(SaveSpendResult.Success, result)
        verify(spendRepository, never()).isSpendExists(any())
        verify(spendRepository).save(spend)
    }

    @Test
    fun invoke_existingSpend_default(): Unit = runBlocking {
        val id = 1
        val spend = generateSpend(id)
        whenever(spendRepository.isSpendExists(id)).thenReturn(true)
        whenever(spendRepository.save(spend)).thenReturn(Result.success(spend))

        val result = sut(spend)

        assertEquals(SaveSpendResult.Success, result)
        verify(spendRepository).save(spend)
    }

    @Test
    fun invoke_existingSpend_doesNotExist(): Unit = runBlocking {
        val id = 1
        val spend = generateSpend(id)
        whenever(spendRepository.isSpendExists(id)).thenReturn(false)

        val result = sut(spend)

        assertEquals(SaveSpendResult.NotExistingSpendError, result)
        verify(spendRepository, never()).save(spend)
    }

    @Test
    fun invoke_unknownError(): Unit = runBlocking {
        val spend = generateSpend()
        val error = RuntimeException("test")
        whenever(spendRepository.save(spend)).thenReturn(Result.failure(error))

        val result = sut(spend)

        assertEquals(SaveSpendResult.UnknownError(error), result)
    }

    private fun generateSpend(id: Int = EMPTY_ID) = Spend(
        id = id,
        dateMillis = 1L,
        price = 12F,
        currency = Currency.Ruble,
        amount = 1,
        amountType = AmountType.Count,
        product = Product("test"),
        shop = Shop("test shop"),
        comment = "",
    )
}