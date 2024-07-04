package com.example.housekeeper.data.shop

import com.example.housekeeper.domain.shop.Shop
import com.example.housekeeper.utils.anyObject
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class ShopRepositoryImplTest {

    private lateinit var sut: ShopRepositoryImpl
    private lateinit var shopDao: ShopDao

    @Before
    fun setup() {
        shopDao = Mockito.mock()
        sut = ShopRepositoryImpl(shopDao)
    }

    @Test
    fun hasShop_true(): Unit = runBlocking {
        val name = "test name"
        whenever(shopDao.getOrNull(name)).thenReturn(DataShop(name))

        val result = sut.hasShop(name)

        assertTrue(result)
    }

    @Test
    fun hasShop_false(): Unit = runBlocking {
        val name = "test name"
        whenever(shopDao.getOrNull(name)).thenReturn(null)

        val result = sut.hasShop(name)

        assertFalse(result)
    }

    @Test
    fun addShop_default(): Unit = runBlocking {
        val name = "test name"

        val result = sut.addShop(name)

        assertEquals("Не тот результат", Result.success(Shop(name)), result)
        verify(shopDao).insert(DataShop(name))
    }

    @Test
    fun addShop_error(): Unit = runBlocking {
        val name = "test name"
        val error = RuntimeException("test error")
        whenever(shopDao.insert(anyObject())).then {
            throw error
        }

        val result = sut.addShop(name)

        assertEquals(Result.failure<Unit>(error), result)
    }
}