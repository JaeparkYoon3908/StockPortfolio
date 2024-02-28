package com.yjpapp.stockportfolio.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yjpapp.data.model.MyStockData
import com.yjpapp.data.model.NewsData
import com.yjpapp.data.model.ResponseResult
import com.yjpapp.data.repository.MyStockRepository
import com.yjpapp.data.repository.NewsRepository
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.model.DialogType
import com.yjpapp.stockportfolio.model.ToastMessage
import com.yjpapp.stockportfolio.ui.main.news.newsMenuList
import com.yjpapp.stockportfolio.util.StockUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MyStockUiState(
    val totalPurchasePrice: String = "", //상단 총 매수금액
    val totalEvaluationAmount: String = "",
    val totalGainPrice: String = "", //상단 손익
    val totalGainPricePercent: String = "0%", //상단 수익률
    val myStockInfoList: List<MyStockData> = listOf(),
    val isLoading: Boolean = false,
)
data class NewsUiState(
    val newsList: HashMap<String, List<NewsData>> = hashMapOf(),
    val isLoading: Boolean = false
)
/**
 * MainActivity Global ViewModel
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val myStockRepository: MyStockRepository,
    private val newsRepository: NewsRepository
): ViewModel() {
    //나의 주식
    private val _myStockUiState = MutableStateFlow(MyStockUiState(isLoading = false))
    val myStockUiState: StateFlow<MyStockUiState> = _myStockUiState.asStateFlow()
    //경제 뉴스
    private val _newsUiState = MutableStateFlow(NewsUiState(isLoading = false))
    val newsUiState: StateFlow<NewsUiState> = _newsUiState.asStateFlow()
    //메인 다이얼로그
    private val _dialogUiState = MutableSharedFlow<DialogType>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val dialogUiState: SharedFlow<DialogType> = _dialogUiState.asSharedFlow()
    //toast message
    private val _toastMessageState = MutableSharedFlow<ToastMessage?>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val toastMessageState = _toastMessageState.asSharedFlow()

    init {
        viewModelScope.launch {
            getAllMyStock()
            calculateTopData()
        }
    }
    /**
     * 나의 주식
     */
    private fun getAllMyStock() = viewModelScope.launch {
        when (val response = myStockRepository.getAllMyStock()) {
            is ResponseResult.Success -> {
                _myStockUiState.update {
                    it.copy(
                        myStockInfoList = response.data?: listOf(),
                        isLoading = false
                    )
                }
                calculateTopData()
            }
            is ResponseResult.Error -> {
                _toastMessageState.emit(ToastMessage(message = response.resultMessage))
            }
        }
    }

    fun addMyStock(myStockData: MyStockData) = viewModelScope.launch {
        when (val response = myStockRepository.addMyStock(myStockData)) {
            is ResponseResult.Success -> {
                _dialogUiState.emit(DialogType.None)
                getAllMyStock()
                _toastMessageState.emit(
                    ToastMessage(
                        strResId = R.string.Msg_MyStock_Add_Success
                    )
                )
            }
            is ResponseResult.Error -> {
                _toastMessageState.emit(ToastMessage(message = response.resultMessage))
            }
        }
    }

    fun updateMyStock(myStockData: MyStockData) = viewModelScope.launch {
        when (val response = myStockRepository.updateMyStock(myStockData)) {
            is ResponseResult.Success -> {
                _dialogUiState.emit(DialogType.None)
                getAllMyStock()
                _toastMessageState.emit(
                    ToastMessage(
                        strResId = R.string.Msg_MyStock_Update_Success
                    )
                )
            }
            is ResponseResult.Error -> {
                _toastMessageState.emit(ToastMessage(message = response.resultMessage))
            }
        }
    }

    fun deleteMyStock(myStockData: MyStockData) = viewModelScope.launch {
        when (val response = myStockRepository.deleteMyStock(myStockData)) {
            is ResponseResult.Success -> {
                getAllMyStock()
                _toastMessageState.emit(
                    ToastMessage(
                        strResId = R.string.Msg_MyStock_Delete_Success
                    )
                )
            }
            is ResponseResult.Error -> {
                _toastMessageState.emit(ToastMessage(message = response.resultMessage))
            }
        }
    }

    private fun calculateTopData() {
        var mTotalPurchasePrice = 0.00 // 총 매수금액
        var mTotalEvaluationAmount = 0.00 // 총 평가금액
        //계산
        _myStockUiState.value.myStockInfoList.forEach {
            val purchasePrice = StockUtils.getNumDeletedComma(it.purchasePrice).toDouble()
            val currentPrice = StockUtils.getNumDeletedComma(it.currentPrice).toDouble()
            val purchaseCount = it.purchaseCount.toDouble()
            mTotalPurchasePrice += purchasePrice * purchaseCount
            mTotalEvaluationAmount += currentPrice * purchaseCount
        }
        val mTotalGainPrice = mTotalEvaluationAmount - mTotalPurchasePrice //손익
        val mTotalGainPricePercent = StockUtils.calculateGainPercent(mTotalPurchasePrice, mTotalEvaluationAmount) //수익률
        _myStockUiState.update {
            it.copy(
                totalPurchasePrice = mTotalPurchasePrice.toString(),
                totalEvaluationAmount = mTotalEvaluationAmount.toString(),
                totalGainPrice = mTotalGainPrice.toString(),
                totalGainPricePercent = StockUtils.getRoundsPercentNumber(mTotalGainPricePercent),
            )
        }
    }

    fun refreshStockCurrentPriceInfo() = viewModelScope.launch {
        _myStockUiState.update { it.copy(isLoading = true) }
        when (val response = myStockRepository.refreshMyStock()) {
            is ResponseResult.Success -> {
                getAllMyStock()
                _toastMessageState.emit(
                    ToastMessage(
                        strResId = R.string.Msg_Current_Price_Refresh_Success
                    )
                )
            }
            is ResponseResult.Error -> {
                _toastMessageState.emit(ToastMessage(message = response.resultMessage))
            }
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
        _toastMessageState.emit(null)
    }
    fun showMainDialog(dialogType: DialogType) = viewModelScope.launch {
        _dialogUiState.emit(dialogType)
    }
}