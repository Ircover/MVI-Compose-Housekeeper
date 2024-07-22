package com.example.housekeeper.data.spend

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.housekeeper.data.AppDatabase
import com.example.housekeeper.data.product.DataProduct
import com.example.housekeeper.data.shop.DataShop
import com.example.housekeeper.domain.Currency
import com.example.housekeeper.domain.product.AmountType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SpendDaoTests {
    private lateinit var spendDao: SpendDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        spendDao = db.spendDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun writeAndReadSpend(): Unit = runBlocking {
        val spend1 = spendDao.insert(generateSpend(1L))
        spendDao.insert(generateSpend(2L))
        spendDao.insert(generateSpend(3L))

        Assert.assertTrue("Не найден существующий элемент", spendDao.isExist(spend1))
        Assert.assertFalse("Найден несуществующий элемент", spendDao.isExist(4L))

        val allInserted = spendDao.getAll().first()
        Assert.assertEquals("Неверное количество всех трат", 3, allInserted.size)
    }

    private fun generateSpend(id: Long) = DataSpend(
        id = id,
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