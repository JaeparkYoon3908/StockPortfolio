package com.yjpapp.stockportfolio

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import com.yjpapp.data.repository.MyStockRepositoryImpl
import com.yjpapp.data.repository.NewsRepositoryImpl
import com.yjpapp.stockportfolio.ui.main.MainScreen
import com.yjpapp.stockportfolio.ui.main.MainViewModel
import com.yjpapp.stockportfolio.ui.main.NavItem
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class MainUITest {
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