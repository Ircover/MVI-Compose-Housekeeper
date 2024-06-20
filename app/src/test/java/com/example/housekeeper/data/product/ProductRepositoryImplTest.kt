package com.example.housekeeper.data.product

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

class ProductRepositoryImplTest {

    private lateinit var sut: ProductRepositoryImpl
    private lateinit var productDao: ProductDao

    @Before
    fun setup() {
        productDao = Mockito.mock()
        sut = ProductRepositoryImpl(productDao)
    }

    @Test
    fun hasProduct_true(): Unit = runBlocking {
        val name = "test name"
        whenever(productDao.getOrNull(name)).thenReturn(DataProduct(name))

        val result = sut.hasProduct(name)

        assertTrue(result)
    }

    @Test
    fun hasProduct_false(): Unit = runBlocking {
        val name = "test name"
        whenever(productDao.getOrNull(name)).thenReturn(null)

        val result = sut.hasProduct(name)

        assertFalse(result)
    }

    @Test
    fun addProduct_default(): Unit = runBlocking {
        val name = "test name"

        val result = sut.addProduct(name)

        assertEquals("Не тот результат", Result.success(Unit), result)
        verify(productDao).insert(DataProduct(name))
    }

    @Test
    fun addProduct_error(): Unit = runBlocking {
        val name = "test name"
        val error = RuntimeException("test error")
        whenever(productDao.insert(anyObject())).then {
            throw error
        }

        val result = sut.addProduct(name)

        assertEquals(Result.failure<Unit>(error), result)
    }
}