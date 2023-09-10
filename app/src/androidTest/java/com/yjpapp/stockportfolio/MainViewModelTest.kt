package com.yjpapp.stockportfolio

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.yjpapp.stockportfolio.data.localdb.room.mystock.MyStockEntity
import com.yjpapp.stockportfolio.data.repository.MyStockRepositoryImpl
import com.yjpapp.stockportfolio.data.repository.NewsRepositoryImpl
import com.yjpapp.stockportfolio.ui.main.MainViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
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

    /**
     * @link[MainViewModel.addMyStock]
     */
    @Test
    fun test_addMyStock() = runTest {
        val testEntityData = MyStockEntity(
            subjectName = "회사이름",
            subjectCode = "subCode",
            purchaseDate = "2023-04-04",
            purchasePrice = "20,000",
            purchaseCount = 0,
            currentPrice = "25,000",
            dayToDayPrice = "5000",
            dayToDayPercent = "25%",
            basDt = "2023.09.08",
        )
        mainViewModel.addMyStock(testEntityData)
        val allMyStockList = myStockRepository.getAllMyStock()
        assertEquals(true, allMyStockList.any { it.subjectCode == testEntityData.subjectCode})
    }
}