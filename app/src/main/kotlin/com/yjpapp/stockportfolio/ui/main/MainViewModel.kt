package com.yjpapp.stockportfolio.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yjpapp.data.model.MyStockData
import com.yjpapp.data.model.NewsData
import com.yjpapp.data.model.ResponseResult
import com.yjpapp.data.repository.MySettingRepository
import com.yjpapp.data.repository.MyStockRepository
import com.yjpapp.data.repository.NewsRepository
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.model.Country
import com.yjpapp.stockportfolio.ui.main.news.newsMenuList
import com.yjpapp.stockportfolio.util.StockUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MainUiState(
    val toastMessageId: Int = 0,
    val toastErrorMessage: String? = null,
    val isLoading: Boolean = false, //전체 화면 로딩 여부
)

data class MyStockUiState(
    val type: Int = 0,
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
    private val myStockRepository: MyStockRepository,
    private val newsRepository: NewsRepository,
    private val mySettingRepository: MySettingRepository,
) : ViewModel() {
    //전체 메인 화면
    private val _mainUiState = MutableStateFlow(MainUiState())
    val mainUiState: StateFlow<MainUiState> = _mainUiState.asStateFlow()

    //나의 주식 tab
    private val _myStockUiState = MutableStateFlow(MyStockUiState())
    val myStockUiState: StateFlow<MyStockUiState> = _myStockUiState.asStateFlow()

    //경제 뉴스 tab
    private val _newsUiState = MutableStateFlow(NewsUiState())
    val newsUiState: StateFlow<NewsUiState> = _newsUiState.asStateFlow()

    private val _myStockCountryState = MutableStateFlow(Country.Korea)
    val myStockCountryState: StateFlow<Country> = _myStockCountryState.asStateFlow()

    /**
     * 나의 주식
     */
    init {
        viewModelScope.launch {
            val defaultTitle = getDefaultMyStockTitle()
            _myStockCountryState.update { Country.entries.find { it.title == defaultTitle }?: Country.Korea }
            getAllMyStock(type = _myStockCountryState.value.type)
        }
    }

    /**
     * type 1: 한국주식, 2: 미국주식
     */
    fun getAllMyStock(type: Int) = viewModelScope.launch {
        when (val result = myStockRepository.getAllMyStock()) {
            is ResponseResult.Success -> {
                var mTotalPurchasePrice = 0.00 // 총 매수금액
                var mTotalEvaluationAmount = 0.00 // 총 평가금액
                //한국주식 리스트
                val koreaStockInfoList = result.data?.filter { data -> data.type == 1 }?: listOf()
                //미국 주식 리스트
                val usaStockInfoList = result.data?.filter { data -> data.type == 2 }?: listOf()
                val stockInfoList = if (type == 1) koreaStockInfoList else usaStockInfoList
                //계산
                stockInfoList.forEach {
                    val purchasePrice = StockUtils.getNumDeletedComma(it.purchasePrice).toDouble()
                    val currentPrice = StockUtils.getNumDeletedComma(it.currentPrice).toDouble()
                    val purchaseCount = it.purchaseCount.toDouble()
                    mTotalPurchasePrice += purchasePrice * purchaseCount
                    mTotalEvaluationAmount += currentPrice * purchaseCount
                }
                //손익
                val mTotalGainPrice = mTotalEvaluationAmount - mTotalPurchasePrice
                //수익률
                val mTotalGainPricePercent =
                    StockUtils.calculateGainPercent(mTotalPurchasePrice, mTotalEvaluationAmount)

                _myStockUiState.update {
                    it.copy(
                        type = type,
                        koreaStockInfoList = koreaStockInfoList,
                        usaStockInfoList = usaStockInfoList,
                        totalPurchasePrice = mTotalPurchasePrice.toString(),
                        totalEvaluationAmount = mTotalEvaluationAmount.toString(),
                        totalGainPrice = mTotalGainPrice.toString(),
                        totalGainPricePercent = StockUtils.getRoundsPercentNumber(mTotalGainPricePercent),
                    )
                }
            }

            is ResponseResult.Error -> {
                _mainUiState.update { it.copy(toastErrorMessage = result.resultMessage) }
            }
        }
    }

    suspend fun getDefaultMyStockTitle(): String {
        return when (val result = mySettingRepository.getDefaultMyStockTitle()) {
            is ResponseResult.Success -> {
                result.data?: myStockCountryList.first().title
            }

            is ResponseResult.Error -> {
                myStockCountryList.first().title
            }
        }
    }

    fun addMyStock(myStockData: MyStockData) = viewModelScope.launch {
        try {
            myStockRepository.addMyStock(myStockData)
            _mainUiState.update {
                it.copy(
                    toastMessageId = R.string.MyStockFragment_Msg_MyStock_Add_Success
                )
            }
            getAllMyStock(type = myStockData.type)
        } catch (e: Exception) {
            _mainUiState.update {
                it.copy(
                    toastMessageId = R.string.Error_Msg_Normal,
                    toastErrorMessage = e.message?: "Unknown error"
                )
            }
        }
    }

    fun updateMyStock(myStockData: MyStockData) = viewModelScope.launch {
        try {
            myStockRepository.updateMyStock(myStockData)
            _mainUiState.update {
                it.copy(
                    toastMessageId = R.string.MyStockFragment_Msg_MyStock_Modify_Success,
                    isLoading = false
                )
            }
            getAllMyStock(type = myStockData.type)
        } catch (e: Exception) {
            _mainUiState.update {
                it.copy(
                    toastMessageId = R.string.Error_Msg_Normal,
                    toastErrorMessage = e.message?: "Unknown error",
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
                    toastMessageId = R.string.MyStockFragment_Msg_MyStock_Delete_Success,
                    isLoading = false
                )
            }
            getAllMyStock(type = myStockData.type)
        } catch (e: Exception) {
            e.stackTrace
            _mainUiState.update {
                it.copy(
                    toastMessageId = R.string.Common_Cancel,
                    toastErrorMessage = e.message?: "Unknown error",
                    isLoading = false
                )
            }
        }
    }

    fun refreshStockCurrentPriceInfo(type: Int) = viewModelScope.launch {
        _mainUiState.update { it.copy(isLoading = true) }
        if (myStockRepository.refreshMyStock(type = type)) {
            _mainUiState.update {
                it.copy(
                    toastMessageId = R.string.Msg_Refresh_Success,
                    isLoading = false
                )
            }
            getAllMyStock(type = type)
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
                    val newsList = result.data ?: listOf()
                    _newsUiState.update {
                        it.copy(
                            newsList = it.newsList.apply { this[tabData.route] = newsList },
                            isLoading = false
                        )
                    }
                }

                is ResponseResult.Error -> {
                    _mainUiState.update {
                        it.copy(
                            toastMessageId = R.string.Error_Msg_Normal,
                            toastErrorMessage = result.resultMessage
                        )
                    }
                }
            }
        }
    }

    fun toastMessageShown() {
        _mainUiState.update { it.copy(toastMessageId = 0, toastErrorMessage = null) }
    }

    fun updateMyStockCountry(country: Country) {
        _myStockCountryState.update { country }
    }

    fun setDefaultMyStockCountry(title: String) = viewModelScope.launch {
        mySettingRepository.setDefaultMyStockTitle(title)
    }
}