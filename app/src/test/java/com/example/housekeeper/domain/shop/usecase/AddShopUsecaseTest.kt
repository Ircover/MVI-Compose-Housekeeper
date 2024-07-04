package com.example.housekeeper.domain.shop.usecase

import com.example.housekeeper.domain.shop.Shop
import com.example.housekeeper.domain.shop.repository.ShopRepository
import com.example.housekeeper.utils.anyObject
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class AddShopUsecaseTest {

    private lateinit var sut: AddShopUsecase
    private lateinit var shopRepository: ShopRepository

    @Before
    fun setup() {
        shopRepository = mock()
        sut = AddShopUsecase(shopRepository)
    }

    @Test
    fun invoke_default(): Unit = runBlocking {
        val name = "testName"
        val shop = Shop(name)
        whenever(shopRepository.hasShop(name)).thenReturn(false)
        whenever(shopRepository.addShop(anyObject())).thenReturn(Result.success(shop))

        val result = sut.invoke(name)

        assertEquals("Не тот результат", AddShopResult.Success(shop), result)
        verify(shopRepository).addShop(name)
    }

    @Test
    fun invoke_existingShop(): Unit = runBlocking {
        val name = "testName"
        whenever(shopRepository.hasShop(name)).thenReturn(true)

        val result = sut.invoke(name)

        assertEquals("Не тот результат", AddShopResult.ShopAlreadyExists, result)
        verify(shopRepository, never()).addShop(anyObject())
    }

    @Test
    fun invoke_error(): Unit = runBlocking {
        val name = "testName"
        val error = RuntimeException("test error")
        whenever(shopRepository.hasShop(name)).thenReturn(false)
        whenever(shopRepository.addShop(anyObject())).thenReturn(Result.failure(error))

        val result = sut.invoke(name)

        assertEquals("Не тот результат", AddShopResult.UnknownError(error), result)
    }
}