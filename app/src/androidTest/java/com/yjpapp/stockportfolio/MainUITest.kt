package com.yjpapp.stockportfolio

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.yjpapp.stockportfolio.data.repository.MyStockRepositoryImpl
import com.yjpapp.stockportfolio.data.repository.NewsRepositoryImpl
import com.yjpapp.stockportfolio.ui.main.MainScreen
import com.yjpapp.stockportfolio.ui.main.MainViewModel
import com.yjpapp.stockportfolio.ui.main.NavItem
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class MainUITest {
    //TODO ViewModelTest에서 중복 되는 코드에 대한 커스텀 룰 있는지 확인
    @get:Rule
    val hiltRule = HiltAndroidRule(this)
    @get:Rule
    val composeTestRule = createComposeRule()
    private lateinit var context: Context

    @Inject
    lateinit var myStockRepository: MyStockRepositoryImpl
    @Inject
    lateinit var newsRepository: NewsRepositoryImpl
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

        composeTestRule.setContent {
            MainScreen(
                viewModel = mainViewModel
            )
        }
    }

    @Test
    fun test_news_navigation_click() {
        composeTestRule
            .onNodeWithContentDescription(context.getString(NavItem.News.title))
            .performClick()
        composeTestRule
            .onNodeWithContentDescription("NewsTab")
            .assertExists()
        Thread.sleep(3000)
    }

}