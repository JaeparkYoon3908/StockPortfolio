package com.yjpapp.stockportfolio.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yjpapp.data.model.ResponseResult
import com.yjpapp.data.model.request.ReqKoreaStockPriceInfo
import com.yjpapp.data.model.response.StockPriceData
import com.yjpapp.data.model.response.UsaStockSymbolData
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
    val usaStockSearchResult: List<UsaStockSymbolData> = listOf(),
    val koreaStockSearchResult: List<StockPriceData> = listOf(),
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

    fun requestKoreaSearchList(keyWord: String) = viewModelScope.launch {
        _uiState.update { it.copy(koreaStockSearchResult = listOf(), isLoading = true) }
        val reqKoreaStockPriceInfo = ReqKoreaStockPriceInfo(
            numOfRows = "50",
            pageNo = "1",
            likeItmsNm = keyWord
        )

        when (val result = myStockRepository.getKoreaStockPriceInfo(reqKoreaStockPriceInfo)) {
            is ResponseResult.Success -> {
                val companyList = result.data?: listOf()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        koreaStockSearchResult = companyList.distinct()
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
    fun requestUsaSearchList(keywords: String) = viewModelScope.launch {
        _uiState.update { it.copy(usaStockSearchResult = listOf(), isLoading = true) }
        when (val result = myStockRepository.getUsaStockSymbol(keywords = keywords)) {
            is ResponseResult.Success -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        usaStockSearchResult = result.data?: listOf()
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
}