package com.yjpapp.stockportfolio.ui.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yjpapp.data.model.MyStockData
import com.yjpapp.data.model.NewsData
import com.yjpapp.data.model.ResponseResult
import com.yjpapp.data.repository.MyStockRepository
import com.yjpapp.data.repository.NewsRepository
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.ui.main.news.newsMenuList
import com.yjpapp.stockportfolio.util.StockUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MainUiState(
    val toastMessage: String? = null,
    val isLoading: Boolean = false, //전체 화면 로딩 여부
)
data class MyStockUiState(
    val totalPurchasePrice: String = "", //상단 총 매수금액
    val totalEvaluationAmount: String = "",
    val totalGainPrice: String = "", //상단 손익
    val totalGainPricePercent: String = "0%", //상단 수익률
    val koreaStockInfoList: List<MyStockData> = listOf(),
    val usaStockInfoList: List<MyStockData> = listOf()
)
data class NewsUiState(
    val newsList: HashMap<String, List<NewsData>> = hashMapOf(),
    val isLoading: Boolean = false, //일부 뉴스 탭 영역 로딩 애니메이션 노출 여부
)

/**
 * MainActivity Global ViewModel
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val myStockRepository: MyStockRepository,
    private val newsRepository: NewsRepository
): ViewModel() {
    //전체 메인 화면
    private val _mainUiState = MutableStateFlow(MainUiState(isLoading = false, toastMessage = null))
    val mainUiState: StateFlow<MainUiState> = _mainUiState.asStateFlow()
    //나의 주식 tab
    private val _myStockUiState = MutableStateFlow(MyStockUiState())
    val myStockUiState: StateFlow<MyStockUiState> = _myStockUiState.asStateFlow()
    //경제 뉴스 tab
    private val _newsUiState = MutableStateFlow(NewsUiState())
    val newsUiState: StateFlow<NewsUiState> = _newsUiState.asStateFlow()

    /**
     * 나의 주식
     */
    init {
        viewModelScope.launch { getAllMyStock() }
    }

    /**
     * type 1: 한국주식, 2: 미국주식
     */
    private fun getAllMyStock() = viewModelScope.launch {
        when (val result = myStockRepository.getAllMyStock()) {
            is ResponseResult.Success -> {
                _myStockUiState.update {
                    it.copy(
                        koreaStockInfoList = result.data?.filter { data-> data.type == 1 }?: listOf(),
                        usaStockInfoList = result.data?.filter { data-> data.type == 2 }?: listOf(),
                    )
                }
                calculateTopData()
            }
            is ResponseResult.Error -> {
                _mainUiState.update { it.copy(toastMessage = result.resultMessage) }
            }
        }
    }

    fun addMyStock(myStockData: MyStockData) = viewModelScope.launch {
        try {
            myStockRepository.addMyStock(myStockData)
            _mainUiState.update {
                it.copy(
                    toastMessage = context.getString(R.string.MyStockFragment_Msg_MyStock_Add_Success)
                )
            }
            getAllMyStock()
        } catch (e: Exception) {
            _mainUiState.update {
                it.copy(
                    toastMessage = "${context.getString(R.string.Error_Msg_Normal)} cause : ${e.message}"
                )
            }
        }
    }

    fun updateMyStock(myStockData: MyStockData) = viewModelScope.launch {
        try {
            myStockRepository.updateMyStock(myStockData)
            _mainUiState.update {
                it.copy(
                    toastMessage = context.getString(R.string.MyStockFragment_Msg_MyStock_Modify_Success),
                    isLoading = false
                )
            }
            getAllMyStock()
        } catch (e: Exception) {
            _mainUiState.update {
                it.copy(
                    toastMessage = "${context.getString(R.string.Error_Msg_Normal)} cause : ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    fun deleteMyStock(myStockData: MyStockData) = viewModelScope.launch {
        try {
            myStockRepository.deleteMyStock(myStockData)
            _mainUiState.update {
                it.copy(
                    toastMessage = context.getString(R.string.MyStockFragment_Msg_MyStock_Delete_Success),
                    isLoading = false
                )
            }
            getAllMyStock()
        } catch (e: Exception) {
            e.stackTrace
            _mainUiState.update {
                it.copy(
                    toastMessage = context.getString(R.string.Common_Cancel),
                    isLoading = false
                )
            }
        }
    }

    private fun calculateTopData() {
        var mTotalPurchasePrice = 0.00 // 총 매수금액
        var mTotalEvaluationAmount = 0.00 // 총 평가금액
        var mTotalGainPrice = 0.00 //손익
        var mTotalGainPricePercent = 0.00 //수익률

        _myStockUiState.value.koreaStockInfoList.forEach {
            val purchasePrice = StockUtils.getNumDeletedComma(it.purchasePrice).toDouble()
            val currentPrice = StockUtils.getNumDeletedComma(it.currentPrice).toDouble()
            val purchaseCount = it.purchaseCount.toDouble()
            mTotalPurchasePrice += purchasePrice * purchaseCount
            mTotalEvaluationAmount += currentPrice * purchaseCount
        }
        mTotalGainPrice = mTotalEvaluationAmount - mTotalPurchasePrice
        mTotalGainPricePercent = StockUtils.calculateGainPercent(mTotalPurchasePrice, mTotalEvaluationAmount)
        _myStockUiState.update {
            it.copy(
                totalPurchasePrice = mTotalPurchasePrice.toString(),
                totalEvaluationAmount = mTotalEvaluationAmount.toString(),
                totalGainPrice = mTotalGainPrice.toString(),
                totalGainPricePercent = StockUtils.getRoundsPercentNumber(mTotalGainPricePercent),
            )
        }
    }

    fun refreshStockCurrentPriceInfo(type: Int) = viewModelScope.launch {
        _mainUiState.update { it.copy(isLoading = true) }
        if (myStockRepository.refreshMyStock(type = type)) {
            _mainUiState.update {
                it.copy(
                    toastMessage = "새로고침 되었습니다.",
                    isLoading = false
                )
            }
            getAllMyStock()
        }
    }

    /**
     * 경제 뉴스
     */
    fun getNewsList() = viewModelScope.launch {
        _newsUiState.update { it.copy(isLoading = true) }
        newsMenuList.forEach { tabData ->
            when (val result = newsRepository.getNewsList(tabData.url)) {
                is ResponseResult.Success -> {
                    val newsList = result.data?: listOf()
                    _newsUiState.update {
                        it.copy(
                            newsList = it.newsList.apply { this[tabData.route] = newsList },
                            isLoading = false
                        )
                    }
                }
                is ResponseResult.Error -> {

                }
            }
        }
    }
    fun toastMessageShown() = viewModelScope.launch {
        _mainUiState.update { it.copy(toastMessage = null) }
    }
}