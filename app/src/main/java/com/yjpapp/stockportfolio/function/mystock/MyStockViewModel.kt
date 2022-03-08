package com.yjpapp.stockportfolio.function.mystock


import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.opencsv.CSVReader
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.extension.MutableEventFlow
import com.yjpapp.stockportfolio.extension.asEventFlow
import com.yjpapp.stockportfolio.localdb.room.mystock.MyStockEntity
import com.yjpapp.stockportfolio.model.SubjectName
import com.yjpapp.stockportfolio.repository.MyStockRepository
import com.yjpapp.stockportfolio.util.StockUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
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
//        getCurrentPrices()
        calculateTopData()
    }

    fun addMyStock(context: Context, myStockEntity: MyStockEntity): Boolean {
        return try {
            myStockRepository.insertMyStock(myStockEntity)
            myStockRepository.getAllMyStock().last {
                myStockInfoList.add(it)
            }
            _scrollIndex.value = myStockInfoList.size
            event(Event.ShowInfoToastMessage("추가 완료 됐습니다."))
            calculateTopData()
            true
        } catch (e: Exception) {
            e.stackTrace
            event(Event.ShowErrorToastMessage(context.getString(R.string.MyStockInputDialog_Error_Message)))
            false
        }
    }

    fun updateMyStock(context: Context, myStockEntity: MyStockEntity): Boolean {
        return try {
            myStockRepository.updateMyStock(myStockEntity)
            myStockInfoList.clear()
            myStockInfoList.addAll(myStockRepository.getAllMyStock().toMutableStateList())
            event(Event.ShowInfoToastMessage("수정 완료 됐습니다."))
            calculateTopData()
            true
        } catch (e: Exception) {
            e.stackTrace
            event(Event.ShowErrorToastMessage(context.getString(R.string.MyStockInputDialog_Error_Message)))
            false
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
        var totalPurchasePrice = 0.00 // 총 매수금액
        var totalEvaluationAmount = 0.00 // 총 평가금액
        var totalGainPrice = 0.00 //손익
        var totalGainPricePercent = 0.00 //수익률

        myStockInfoList.forEach {
            val purchasePrice = StockUtils.getNumDeletedComma(it.purchasePrice).toDouble()
            val currentPrice = StockUtils.getNumDeletedComma(it.currentPrice).toDouble()
            val purchaseCount = it.purchaseCount.toDouble()
            totalPurchasePrice += purchasePrice * purchaseCount
            totalEvaluationAmount += currentPrice * purchaseCount
        }
        totalGainPrice = totalEvaluationAmount - totalPurchasePrice

        viewModelScope.launch {
            _totalPurchasePrice.emit(totalPurchasePrice.toString())
            _totalEvaluationAmount.emit(totalEvaluationAmount.toString())
            _totalGainPrice.emit(totalGainPrice.toString())
        }
    }

    fun getAllCurrentPrices() {
        viewModelScope.launch {
            repeat(myStockInfoList.size) { count ->
                val url = "https://finance.naver.com/item/main.naver?code=${myStockInfoList[count].subjectCode}"
                val doc = withContext(Dispatchers.IO) {
                    try {
                        Jsoup.connect(url).get()
                    } catch (e: Exception) {
                        e.stackTrace
                        null
                    }
                }
                val blind = doc?.select(".blind")
                blind?.let {
                    if (it.isNotEmpty() && it.size > 19) {
                        val startIndex = it.size - 18
                        var currentPrice = it[startIndex].text()
                        var dayToDayPrice = it[startIndex + 1].text()
                        var dayToDayPercent = it[startIndex + 2].text()
                        var yesterdayPrice = it[startIndex + 3].text()
                        myStockInfoList[count].currentPrice = currentPrice
                    }
                }
            }
        }
    }

    suspend fun getCurrentPrice(subjectCode: String): String {
        var currentPrice = ""
        val job = viewModelScope.async {
            getCurrentPriceJob(subjectCode = subjectCode)
        }
        currentPrice = job.await()
        return currentPrice
    }

    private suspend fun getCurrentPriceJob(subjectCode: String): String {
        var currentPrice = ""
        var dayToDayPrice = ""
        var dayToDayPercent = ""
        var yesterdayPrice = ""
        val url = "https://finance.naver.com/item/main.naver?code=$subjectCode"
        val doc = withContext(Dispatchers.IO) {
            try {
                Jsoup.connect(url).get()
            } catch (e: Exception) {
                e.stackTrace
                null
            }
        }
        val blind = doc?.select(".blind")
        blind?.let {
            if (it.isNotEmpty() && it.size > 19) {
                val startIndex = it.size - 18
                currentPrice = blind[startIndex].text()
                dayToDayPrice = blind[startIndex + 1].text()
                dayToDayPercent = blind[startIndex + 2].text()
                yesterdayPrice = blind[startIndex + 3].text()
            }
        }
        //TODO currentPrice값이 빈 값일 때 예외처리
        return currentPrice
    }

    /**
     * Event 정의
     */
    private fun event(event: Event) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }

    sealed class Event {
        data class ShowInfoToastMessage(val msg: String): Event()
        data class ShowErrorToastMessage(val msg: String): Event()
        data class ShowLoadingImage(val msg: Unit): Event()
        data class HideLoadingImage(val msg: Unit): Event()
    }
}