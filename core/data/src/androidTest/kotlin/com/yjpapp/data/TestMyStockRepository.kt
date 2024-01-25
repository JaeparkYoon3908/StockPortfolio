@file:OptIn(ExperimentalCoroutinesApi::class)

package com.yjpapp.data

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.yjpapp.data.model.MyStockData
import com.yjpapp.data.repository.MyStockRepositoryImpl
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject


@HiltAndroidTest
class TestMyStockRepository {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)
    @Inject
    lateinit var myStockRepository: MyStockRepositoryImpl
    private lateinit var context: Context
    private val testEntityData = MyStockData(
        id = 124241,
        subjectName = "회사이름",
        subjectCode = "subCode",
//        purchaseDate = "2023-04-04",
        purchasePrice = "20,000",
        purchaseCount = 2,
        currentPrice = "25,000",
        dayToDayPrice = "5000",
        dayToDayPercent = "25%",
        basDt = "2023.09.08",
    )
    @Before
    fun init() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        hiltRule.inject()

    }

    @After
    fun destroy() = runTest {
        myStockRepository.deleteMyStock(testEntityData)
    }

    /**
     * @link[MyStockRepositoryImpl.addMyStock]
     */
    @Test
    fun test_add_my_stock() = runTest {
        //add
        myStockRepository.addMyStock(testEntityData)
        val myStockList = myStockRepository.getAllMyStock().data
        val isAddComplete = myStockList?.any { it.subjectCode == testEntityData.subjectCode }
        Assert.assertEquals(true, isAddComplete)
    }

    @Test
    fun test_update_my_stock() = runTest {
        myStockRepository.addMyStock(testEntityData)
        val updateData = testEntityData.copy(subjectName = "변경")
        myStockRepository.updateMyStock(updateData)
        val myStockList = myStockRepository.getAllMyStock().data //리스트 갱신
        val isUpdateComplete = myStockList?.any { it.id == updateData.id && it.subjectName == updateData.subjectName }
        Assert.assertEquals(true, isUpdateComplete)
    }

    @Test
    fun test_delete_my_stock() = runTest {
        myStockRepository.addMyStock(testEntityData)
        myStockRepository.deleteMyStock(testEntityData)
        val myStockList = myStockRepository.getAllMyStock().data
        val isDeleteComplete = myStockList?.none { it.id == testEntityData.id }
        Assert.assertEquals(true, isDeleteComplete)
    }
}