package com.yjpapp.stockportfolio

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.yjpapp.database.mystock.MyStockEntity
import com.yjpapp.data.repository.MyStockRepositoryImpl
import com.yjpapp.data.repository.NewsRepositoryImpl
import com.yjpapp.stockportfolio.ui.main.MainViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class MainViewModelTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)
    @Inject
    lateinit var myStockRepository: MyStockRepositoryImpl
    @Inject
    lateinit var newsRepository: NewsRepositoryImpl
    private lateinit var context: Context
    private lateinit var mainViewModel: MainViewModel
    private val testEntityData = MyStockEntity(
        id = 124241,
        subjectName = "회사이름",
        subjectCode = "subCode",
        purchaseDate = "2023-04-04",
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
        mainViewModel = MainViewModel(
            context = context,
            myStockRepository = myStockRepository,
            newsRepository = newsRepository
        )
    }

    @After
    fun destroy() = runTest {
        mainViewModel.deleteMyStock(testEntityData)
    }

    /**
     * @link[MainViewModel.addMyStock]
     */
    @Test
    fun test_add_my_stock() = runTest {
        //add
        mainViewModel.addMyStock(testEntityData)
        val myStockList = myStockRepository.getAllMyStock()
        val isAddComplete = myStockList.any { it.subjectCode == testEntityData.subjectCode }
        assertEquals(true, isAddComplete)
    }

    @Test
    fun test_update_my_stock() = runTest {
        mainViewModel.addMyStock(testEntityData)
        testEntityData.subjectName = "변경"
        mainViewModel.updateMyStock(testEntityData)
        val myStockList = myStockRepository.getAllMyStock() //리스트 갱신
        val isUpdateComplete = myStockList.any { it.id == testEntityData.id && it.subjectName == testEntityData.subjectName }
        assertEquals(true, isUpdateComplete)
    }

    @Test
    fun test_delete_my_stock() = runTest {
        mainViewModel.addMyStock(testEntityData)
        mainViewModel.deleteMyStock(testEntityData)
        val myStockList = myStockRepository.getAllMyStock()
        val isDeleteComplete = myStockList.none { it.id == testEntityData.id }
        assertEquals(true, isDeleteComplete)
    }
}