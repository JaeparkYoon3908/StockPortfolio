package com.yjpapp.stockportfolio.ui.mystock.search

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.opencsv.CSVReader
import com.yjpapp.stockportfolio.data.datasource.MyStockDataSource
import com.yjpapp.stockportfolio.data.model.SubjectName
import com.yjpapp.stockportfolio.R
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
    private val myStockRepository: MyStockDataSource
): ViewModel() {
    private var _allStockList: MutableList<Array<String>> = mutableListOf()
    var searchResult = mutableStateListOf<SubjectName>()
        private set

    fun initAllStockList() {
        val inputStreamReader = InputStreamReader(context.resources.openRawResource(R.raw.korea_all_stock_list),"EUC-KR")
        val bufferedReader = BufferedReader(inputStreamReader)
        val csvReader = CSVReader(bufferedReader)
        _allStockList = csvReader.readAll().toMutableList()
    }

    fun requestSearchList(keyWord: String) {
        searchResult.clear()
        _allStockList.forEach {
            if (it[3].contains(other = keyWord, ignoreCase = true)) {
                searchResult.add(
                    SubjectName(
                    code = remakeSubjectCode(it[1]),
                    text = it[3]
                )
                )
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