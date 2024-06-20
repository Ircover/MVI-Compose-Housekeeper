package com.example.housekeeper.data.product

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.housekeeper.data.AppDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProductDaoTests {
    private lateinit var productDao: ProductDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        productDao = db.productDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun writeAndReadProduct(): Unit = runBlocking {
        listOf("test1", "test2", "test3").forEach { name ->
            productDao.insert(DataProduct(name))
        }

        assertNotNull("Не найден существующий элемент", productDao.getOrNull("test1"))
        assertNull("Найден несуществующий элемент", productDao.getOrNull("tes41"))

        val allInserted = productDao.getAll().first()
        assertEquals("Неверное количество всех товаров", 3, allInserted.size)
    }
}