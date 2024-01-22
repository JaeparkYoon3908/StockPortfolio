package com.yjpapp.stockportfolio.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yjpapp.data.model.ResponseResult
import com.yjpapp.data.model.request.ReqStockPriceInfo
import com.yjpapp.data.model.response.StockPriceData
import com.yjpapp.data.repository.MyStockRepository
import com.yjpapp.stockportfolio.model.ErrorUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CompanySearchUiState(
    val searchResult: List<StockPriceData> = listOf(),
    val isLoading: Boolean = false,
    val errorUiState: ErrorUiState = ErrorUiState()
)
/**
 * @author Yoon jaepark
 * @since 2021.11
 */
@HiltViewModel
class CompanySearchViewModel @Inject constructor(
    private val myStockRepository: MyStockRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(CompanySearchUiState(isLoading = false))
    val uiState: StateFlow<CompanySearchUiState> = _uiState.asStateFlow()
    fun requestSearchList(keyWord: String) = viewModelScope.launch {
        _uiState.update { it.copy(searchResult = listOf(), isLoading = true) }
        val reqStockPriceInfo = ReqStockPriceInfo(
            numOfRows = "50",
            pageNo = "1",
            likeItmsNm = keyWord
        )

        when (val result = myStockRepository.searchStockInfo(reqStockPriceInfo)) {
            is ResponseResult.Success -> {
                val companyList = result.data?: listOf()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        searchResult = companyList
                    )
                }
            }
            is ResponseResult.Error -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorUiState = ErrorUiState(
                            isError = true,
                            errorCode = result.resultCode,
                            errorMessage = result.resultMessage,
                        )
                    )
                }
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