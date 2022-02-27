package com.yjpapp.stockportfolio.function.mystock


import android.content.Context
import androidx.compose.runtime.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airbnb.lottie.utils.Utils
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.extension.MutableEventFlow
import com.yjpapp.stockportfolio.extension.asEventFlow
import com.yjpapp.stockportfolio.function.mystock.dialog.MyStockInputDialog
import com.yjpapp.stockportfolio.localdb.room.mystock.MyStockEntity
import com.yjpapp.stockportfolio.repository.MyStockRepository
import com.yjpapp.stockportfolio.util.StockUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyStockViewModel @Inject constructor(
    private val myStockRepository: MyStockRepository
) : ViewModel() {
    private val _eventFlow = MutableEventFlow<Event>()
    val eventFlow = _eventFlow.asEventFlow()
    private val _totalPurchasePrice = MutableStateFlow("")
    val totalPurchasePrice: StateFlow<String> get() = _totalPurchasePrice //상단 총 매수금액
    private val _totalEvaluationAmount = MutableStateFlow("")
    val totalEvaluationAmount: StateFlow<String> get() = _totalEvaluationAmount //상단 총 평가금액
    private val _totalGainPrice = MutableStateFlow("")
    val totalGainPrice: StateFlow<String> get() = _totalGainPrice //상단 손익
    private val _totalGainPricePercent = MutableStateFlow("")
    val totalGainPricePercent: StateFlow<String> get() = _totalGainPricePercent //상단 수익률
    var myStockInfoList = mutableStateListOf<MyStockEntity>() //나의 주식 목록 List
        private set
    private val _scrollIndex by lazy { MutableStateFlow(myStockInfoList.size) }
    val scrollIndex: StateFlow<Int> get() = _scrollIndex

    /**
     * MyStockFragment 영역
     */
    init {
        myStockInfoList = myStockRepository.getAllMyStock().toMutableStateList()
        calculateTopData()
    }

    //확인버튼 클릭 후 Save
    fun saveMyStock(context: Context, myStockEntity: MyStockEntity): Boolean {
        try {
            if (myStockEntity.id == 0) {
                myStockRepository.insertMyStock(myStockEntity)
                myStockRepository.getAllMyStock().last {
                    myStockInfoList.add(it)
                }
                _scrollIndex.value = myStockInfoList.size - 1
                event(Event.ShowInfoToastMessage("추가 완료 됐습니다."))
            } else {
                myStockRepository.updateMyStock(myStockEntity)
                myStockInfoList.clear()
                myStockInfoList.addAll(myStockRepository.getAllMyStock().toMutableStateList())
                event(Event.ShowInfoToastMessage("수정 완료 됐습니다."))
            }
            calculateTopData()
            return true
        } catch (e: Exception) {
            e.stackTrace
            event(Event.ShowErrorToastMessage(context.getString(R.string.MyStockInputDialog_Error_Message)))
            return false
        }
    }

    fun deleteMyStock(myStockEntity: MyStockEntity): Boolean {
        return try {
            myStockRepository.deleteMyStock((myStockEntity))
            myStockInfoList.remove(myStockEntity)
            calculateTopData()
            true
        } catch (e: Exception) {
            e.stackTrace
            false
        }
    }

    private fun calculateTopData() {
        var totalPurchasePrice = 0.00
        var totalEvaluationAmount = 0.00
        var totalGainPrice = 0.00
        var totalGainPricePercent = 0.00

        myStockInfoList.forEach {
            val purchasePrice = StockUtils.getNumDeletedComma(it.purchasePrice).toDouble()
            val purchaseCount = it.purchaseCount.toDouble()
            totalPurchasePrice += purchasePrice * purchaseCount
        }
        viewModelScope.launch {
            val price = StockUtils.getPriceNum(totalPurchasePrice.toString())
            _totalPurchasePrice.emit(price)
        }
    }

    fun getCurrentPrices() {

    }

    private fun event(event: Event) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }

    sealed class Event {
        data class ShowInfoToastMessage(val msg: String): Event()
        data class ShowErrorToastMessage(val msg: String): Event()
    }
}