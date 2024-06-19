package com.example.housekeeper.domain.product.usecase

import com.example.housekeeper.domain.product.repository.ProductRepository
import com.example.housekeeper.utils.anyObject
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class AddProductUsecaseTest {

    private lateinit var sut: AddProductUsecase
    private lateinit var productRepository: ProductRepository

    @Before
    fun setup() {
        productRepository = mock()
        sut = AddProductUsecase(productRepository)
    }

    @Test
    fun invoke_default(): Unit = runBlocking {
        val name = "testName"
        whenever(productRepository.hasProduct(name)).thenReturn(false)
        whenever(productRepository.addProduct(anyObject())).thenReturn(Result.success(Unit))

        val result = sut.invoke(name)

        assertEquals("Не тот результат", AddProductResult.Success, result)
        verify(productRepository).addProduct(name)
    }

    @Test
    fun invoke_existingProduct(): Unit = runBlocking {
        val name = "testName"
        whenever(productRepository.hasProduct(name)).thenReturn(true)

        val result = sut.invoke(name)

        assertEquals("Не тот результат", AddProductResult.ProductAlreadyExists, result)
        verify(productRepository, never()).addProduct(anyObject())
    }

    @Test
    fun invoke_error(): Unit = runBlocking {
        val name = "testName"
        val error = RuntimeException("test error")
        whenever(productRepository.hasProduct(name)).thenReturn(false)
        whenever(productRepository.addProduct(anyObject())).thenReturn(Result.failure(error))

        val result = sut.invoke(name)

        assertEquals("Не тот результат", AddProductResult.UnknownError(error), result)
    }
}