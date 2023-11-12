package com.yjpapp.stockportfolio.ui.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yjpapp.data.model.NewsData
import com.yjpapp.data.model.ResponseResult
import com.yjpapp.data.repository.MyStockRepository
import com.yjpapp.data.repository.NewsRepository
import com.yjpapp.database.mystock.MyStockEntity
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

data class MyStockUiState(
    val totalPurchasePrice: String = "", //상단 총 매수금액
    val totalEvaluationAmount: String = "",
    val totalGainPrice: String = "", //상단 손익
    val totalGainPricePercent: String = "0%", //상단 수익률
    val myStockInfoList: List<MyStockEntity> = listOf(),
    val toastMessage: String = "",
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
    @ApplicationContext private val context: Context,
    private val myStockRepository: MyStockRepository,
    private val newsRepository: NewsRepository
): ViewModel() {
    //나의 주식
    private val _myStockUiState = MutableStateFlow(MyStockUiState(isLoading = false))
    val myStockUiState: StateFlow<MyStockUiState> = _myStockUiState.asStateFlow()
    //경제 뉴스
    private val _newsUiState = MutableStateFlow(NewsUiState(isLoading = false))
    val newsUiState: StateFlow<NewsUiState> = _newsUiState.asStateFlow()

    /**
     * 나의 주식
     */
    init {
        viewModelScope.launch {
            _myStockUiState.update { it.copy(myStockInfoList = myStockRepository.getAllMyStock()) }
            calculateTopData()
        }
    }

    fun addMyStock(myStockEntity: MyStockEntity) = viewModelScope.launch {
        try {
            myStockRepository.addMyStock(myStockEntity)
            _myStockUiState.update {
                it.copy(
                    myStockInfoList = myStockRepository.getAllMyStock(),
                    toastMessage = context.getString(R.string.MyStockFragment_Msg_MyStock_Add_Success)
                )
            }
            calculateTopData()
        } catch (e: Exception) {
            _myStockUiState.update {
                it.copy(
                    toastMessage = "${context.getString(R.string.Error_Msg_Normal)} cause : ${e.message}"
                )
            }
        }
    }

    fun updateMyStock(myStockEntity: MyStockEntity) = viewModelScope.launch {
        try {
            myStockRepository.updateMyStock(myStockEntity)
            _myStockUiState.update {
                it.copy(
                    myStockInfoList = myStockRepository.getAllMyStock(),
                    toastMessage = context.getString(R.string.MyStockFragment_Msg_MyStock_Modify_Success),
                    isLoading = false
                )
            }
            calculateTopData()
        } catch (e: Exception) {
            _myStockUiState.update {
                it.copy(
                    toastMessage = "${context.getString(R.string.Error_Msg_Normal)} cause : ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    fun deleteMyStock(myStockEntity: MyStockEntity) = viewModelScope.launch {
        try {
            myStockRepository.deleteMyStock((myStockEntity))
            _myStockUiState.update {
                it.copy(
                    myStockInfoList = myStockRepository.getAllMyStock(),
                    toastMessage = context.getString(R.string.MyStockFragment_Msg_MyStock_Delete_Success),
                    isLoading = false
                )
            }
            calculateTopData()
        } catch (e: Exception) {
            e.stackTrace
            _myStockUiState.update {
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

        _myStockUiState.value.myStockInfoList.forEach {
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

    fun refreshStockCurrentPriceInfo() = viewModelScope.launch {
        myStockRepository.refreshMyStock()
        _myStockUiState.update {
            it.copy(
                myStockInfoList = myStockRepository.getAllMyStock()
            )
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
}