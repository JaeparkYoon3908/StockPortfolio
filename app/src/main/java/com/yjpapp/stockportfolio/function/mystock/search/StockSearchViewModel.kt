package com.yjpapp.stockportfolio.function.mystock.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.yjpapp.stockportfolio.repository.MyStockRepository

/**
 * @link StockFragment 전용 ViewModel
 *
 * @author Yoon Jae-park
 * @since 2021.11
 */
class StockSearchViewModel(
    application: Application,
    private val myStockRepository: MyStockRepository
) : AndroidViewModel(application) {

    fun requestSearchCompany(company: String) {

    }
}