package com.yjpapp.stockportfolio.ui.main

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.data.localdb.room.mystock.MyStockEntity
import com.yjpapp.stockportfolio.data.model.NewsData
import com.yjpapp.stockportfolio.data.repository.MyStockRepository
import com.yjpapp.stockportfolio.data.repository.NewsRepository
import com.yjpapp.stockportfolio.util.StockUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * MainActivity Global ViewModel
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val myStockRepository: MyStockRepository,
    private val newsRepository: NewsRepository
): ViewModel() {
    private val _uiState = MutableSharedFlow<Event>(
        replay = 0, //replay = 0 : 새로운 구독자에게 이전 이벤트를 전달하지 않음
        extraBufferCapacity = 1, //추가 버퍼를 생성하여 emit 한 데이터가 버퍼에 유지 되도록함
        onBufferOverflow = BufferOverflow.DROP_OLDEST //버퍼가 가득찼을 시 오래된 데이터 제거
    )
    val uiState = _uiState.asSharedFlow() //convert read only
    var totalPurchasePrice = MutableStateFlow("") //상단 총 매수금액
        private set
    var totalEvaluationAmount = MutableStateFlow("")
        private set
    var totalGainPrice = MutableStateFlow("") //상단 손익
        private set
    var totalGainPricePercent = MutableStateFlow("0%") //상단 수익률
        private set
    var myStockInfoList = mutableStateListOf<MyStockEntity>() //나의 주식 목록 List
        private set
    val scrollIndex by lazy { MutableStateFlow(myStockInfoList.size) }
    var newsList = MutableStateFlow<MutableList<NewsData>>(mutableListOf())
        private set

    /**
     * MyStockFragment 영역
     */
    init {
        viewModelScope.launch {
            myStockInfoList.addAll(myStockRepository.getAllMyStock())
            calculateTopData()
        }
    }
    fun getAllMyStock() = viewModelScope.async { myStockRepository.getAllMyStock().toMutableStateList() }

    fun addMyStock(myStockEntity: MyStockEntity) = viewModelScope.launch {
        withContext(viewModelScope.coroutineContext) {
            try {
                myStockRepository.addMyStock(myStockEntity)
                myStockRepository.getAllMyStock().last {
                    myStockInfoList.add(it)
                }
                scrollIndex.value = myStockInfoList.size
                event(Event.ShowInfoToastMessage(context.getString(R.string.MyStockFragment_Msg_MyStock_Add_Success)))
                calculateTopData()
            } catch (e: Exception) {
                e.stackTrace
                event(Event.ShowErrorToastMessage(context.getString(R.string.MyStockInputDialog_Error_Message)))
            }
        }
    }

    fun updateMyStock(myStockEntity: MyStockEntity) = viewModelScope.launch {
        withContext(viewModelScope.coroutineContext) {
            try {
                myStockRepository.updateMyStock(myStockEntity)
                myStockInfoList.clear()
                myStockInfoList.addAll(myStockRepository.getAllMyStock().toMutableStateList())
                event(Event.ShowInfoToastMessage(context.getString(R.string.MyStockFragment_Msg_MyStock_Modify_Success)))
                calculateTopData()
            } catch (e: Exception) {
                e.stackTrace
                event(Event.ShowErrorToastMessage(context.getString(R.string.MyStockInputDialog_Error_Message)))
            }
        }
    }


    suspend fun deleteMyStock(myStockEntity: MyStockEntity) {
        try {
            myStockRepository.deleteMyStock((myStockEntity))
            myStockInfoList.remove(myStockEntity)
            calculateTopData()
        } catch (e: Exception) {
            e.stackTrace
            event(Event.ShowErrorToastMessage(context.getString(R.string.Common_Cancel)))
        }
    }

    private fun calculateTopData() {
        var mTotalPurchasePrice = 0.00 // 총 매수금액
        var mTotalEvaluationAmount = 0.00 // 총 평가금액
        var mTotalGainPrice = 0.00 //손익
        var mTotalGainPricePercent = 0.00 //수익률

        myStockInfoList.forEach {
            val purchasePrice = StockUtils.getNumDeletedComma(it.purchasePrice).toDouble()
            val currentPrice = StockUtils.getNumDeletedComma(it.currentPrice).toDouble()
            val purchaseCount = it.purchaseCount.toDouble()
            mTotalPurchasePrice += purchasePrice * purchaseCount
            mTotalEvaluationAmount += currentPrice * purchaseCount
        }
        mTotalGainPrice = mTotalEvaluationAmount - mTotalPurchasePrice
        mTotalGainPricePercent =
            StockUtils.calculateGainPercent(mTotalPurchasePrice, mTotalEvaluationAmount)
        viewModelScope.launch {
            totalPurchasePrice.emit(mTotalPurchasePrice.toString())
            totalEvaluationAmount.emit(mTotalEvaluationAmount.toString())
            totalGainPrice.emit(mTotalGainPrice.toString())
            totalGainPricePercent.emit(StockUtils.getRoundsPercentNumber(mTotalGainPricePercent))
        }
    }

    fun refreshStockCurrentPriceInfo() = viewModelScope.launch {
        myStockRepository.refreshMyStock()
    }

    fun getNewsList() = CoroutineScope(Dispatchers.IO).launch {
        newsList.value = newsRepository.getMkFinanceNewsList()
    }

    /**
     * Event 정의
     */
    private fun event(event: Event) {
        viewModelScope.launch {
            _uiState.emit(event)
        }
    }

    sealed class Event {
        data class ShowInfoToastMessage(val msg: String) : Event()
        data class ShowErrorToastMessage(val msg: String) : Event()
        data class ShowLoadingImage(val msg: Unit) : Event()
        data class HideLoadingImage(val msg: Unit) : Event()
        data class SuccessIncomeNoteAdd(val data: MyStockEntity) : Event()
        data class RefreshCurrentPriceDone(val isSuccess: Boolean) : Event()
        data class ResponseServerError(val msg: String) : Event()
    }
}