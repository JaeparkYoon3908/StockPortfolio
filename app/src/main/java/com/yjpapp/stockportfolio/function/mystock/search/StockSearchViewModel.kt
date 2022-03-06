package com.yjpapp.stockportfolio.function.mystock.search

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.opencsv.CSVReader
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.model.SubjectName
import com.yjpapp.stockportfolio.repository.MyStockRepository
import com.yjpapp.stockportfolio.util.StockLog
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val myStockRepository: MyStockRepository
): ViewModel() {
    private var _allStockList: MutableList<Array<String>> = mutableListOf()
    val allStockList: List<Array<String>> get() = _allStockList
    var searchResult = mutableStateListOf<SubjectName>()
        private set

    fun initAllStockList(context: Context) {
        val inputStreamReader = InputStreamReader(context.resources.openRawResource(R.raw.korea_all_stock_list),"EUC-KR")
        val bufferedReader = BufferedReader(inputStreamReader)
        val csvReader = CSVReader(bufferedReader)
        _allStockList = csvReader.readAll().toMutableList()
    }

    fun requestSearchList(keyWord: String) {
        searchResult.clear()
        _allStockList.forEach {
            if (it[3].contains(keyWord)) {
                searchResult.add(SubjectName(
                    code = remakeSubjectCode(it[1]),
                    text = it[3]
                ))
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