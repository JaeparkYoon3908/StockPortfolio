package com.yjpapp.stockportfolio.function.mystock.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.yjpapp.stockportfolio.repository.MyStockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @link StockFragment 전용 ViewModel
 *
 * @author Yoon Jae-park
 * @since 2021.11
 */
@HiltViewModel
class StockSearchViewModel @Inject constructor(
    private val myStockRepository: MyStockRepository
): ViewModel() {

    fun requestSearchCompany(company: String) {

    }
}