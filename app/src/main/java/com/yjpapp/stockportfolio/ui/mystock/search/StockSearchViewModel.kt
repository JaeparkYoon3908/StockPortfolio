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
import com.yjpapp.stockportfolio.data.repository.MyStockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

/**
 * @link StockFragment 전용 ViewModel
 *
 * @author Yoon Jae-park
 * @since 2021.11
 */
@HiltViewModel
class StockSearchViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val myStockRepository: MyStockRepository
): ViewModel() {
    var searchResult = mutableStateListOf<SubjectName>()
        private set

    fun requestSearchList(keyWord: String) {
        searchResult.clear()
        viewModelScope.launch {
            val reqStockPriceInfo = ReqStockPriceInfo(
                numOfRows = "20",
                pageNo = "1",
                basDt = "20230824",
                likeItmsNm = keyWord
            )
            val result = myStockRepository.getStockPriceInfo(reqStockPriceInfo)
            if (result.isSuccessful) {
                val a = result.data?.response?.body?.items?.item
                Log.d("YJP", "a = ${a?.toString()}")
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