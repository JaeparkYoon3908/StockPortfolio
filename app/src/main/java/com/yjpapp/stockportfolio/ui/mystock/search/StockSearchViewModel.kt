package com.yjpapp.stockportfolio.ui.mystock.search

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.opencsv.CSVReader
import com.yjpapp.stockportfolio.data.datasource.MyStockRoomDataSource
import com.yjpapp.stockportfolio.data.model.SubjectName
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.data.datasource.StockInfoDataSource
import com.yjpapp.stockportfolio.data.model.request.ReqStockPriceInfo
import com.yjpapp.stockportfolio.data.model.response.StockPriceInfo
import com.yjpapp.stockportfolio.data.repository.MyStockRepository
import com.yjpapp.stockportfolio.util.StockUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

/**
 * @author Yoon jaepark
 * @since 2021.11
 */
@HiltViewModel
class StockSearchViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val myStockRepository: MyStockRepository
): ViewModel() {
    var searchResult = mutableStateListOf<StockPriceInfo>()
        private set

    fun requestSearchList(keyWord: String) {
        searchResult.clear()
        viewModelScope.launch {
            val reqStockPriceInfo = ReqStockPriceInfo(
                numOfRows = "50",
                pageNo = "1",
                likeItmsNm = keyWord
            )
            val result = myStockRepository.getStockPriceInfo(reqStockPriceInfo)
            if (result.isSuccessful && result.data != null) {
                searchResult.addAll(result.data.response.body.items.item)
            }
        }
    }
    //종목 코드 6자리 만들기
    private fun remakeSubjectCode(code: String): String {
        val result = StringBuffer()
        val repeatCount = 6 - code.length
        for (i in 0 until repeatCount) {
            result.append("0")
        }
        result.append(code)
        return result.toString()
    }
}