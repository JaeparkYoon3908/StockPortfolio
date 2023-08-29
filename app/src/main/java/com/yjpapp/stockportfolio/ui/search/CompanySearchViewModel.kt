package com.yjpapp.stockportfolio.ui.search

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yjpapp.stockportfolio.data.model.request.ReqStockPriceInfo
import com.yjpapp.stockportfolio.data.model.response.StockPriceInfo
import com.yjpapp.stockportfolio.data.repository.MyStockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Yoon jaepark
 * @since 2021.11
 */
@HiltViewModel
class CompanySearchViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val myStockRepository: MyStockRepository
): ViewModel() {
    var searchResult = mutableStateListOf<StockPriceInfo>()
        private set
    var isLoading = MutableStateFlow(false)
        private set
    fun requestSearchList(keyWord: String) {
        searchResult.clear()
        isLoading.value = true
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
            isLoading.value = false
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