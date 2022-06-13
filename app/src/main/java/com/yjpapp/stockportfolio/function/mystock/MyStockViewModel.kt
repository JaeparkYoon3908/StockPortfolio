package com.yjpapp.stockportfolio.function.mystock


import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewModelScope
import com.yjpapp.data.datasource.PreferenceDataSource
import com.yjpapp.data.localdb.preference.PrefKey
import com.yjpapp.data.localdb.room.mystock.MyStockEntity
import com.yjpapp.data.model.request.ReqIncomeNoteInfo
import com.yjpapp.data.repository.IncomeNoteRepository
import com.yjpapp.data.repository.MyStockRepository
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.base.BaseViewModel
import com.yjpapp.stockportfolio.common.StockConfig
import com.yjpapp.stockportfolio.extension.EventFlow
import com.yjpapp.stockportfolio.extension.MutableEventFlow
import com.yjpapp.stockportfolio.util.NetworkUtils
import com.yjpapp.stockportfolio.util.StockUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import javax.inject.Inject

@HiltViewModel
class MyStockViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val myStockRepository: MyStockRepository,
    private val incomeNoteRepository: IncomeNoteRepository,
    private val preferenceRepository: PreferenceDataSource
) : BaseViewModel() {
    private val _uiState = MutableEventFlow<Event>()
    val uiState: EventFlow<Event> get() = _uiState
    private val _totalPurchasePrice = MutableStateFlow("")
    val totalPurchasePrice: StateFlow<String> get() = _totalPurchasePrice //상단 총 매수금액
    private val _totalEvaluationAmount = MutableStateFlow("")
    val totalEvaluationAmount: StateFlow<String> get() = _totalEvaluationAmount //상단 총 평가금액
    private val _totalGainPrice = MutableStateFlow("")
    val totalGainPrice: StateFlow<String> get() = _totalGainPrice //상단 손익
    private val _totalGainPricePercent = MutableStateFlow("0%")
    val totalGainPricePercent: StateFlow<String> get() = _totalGainPricePercent //상단 수익률
    var myStockInfoList = mutableStateListOf<MyStockEntity>() //나의 주식 목록 List
        private set
    private val _scrollIndex by lazy { MutableStateFlow(myStockInfoList.size) }
    val scrollIndex: StateFlow<Int> get() = _scrollIndex
    var isCurrentPriceRefreshing = false

    /**
     * MyStockFragment 영역
     */
    init {
        viewModelScope.launch {
            myStockInfoList = myStockRepository.getAllMyStock().toMutableStateList()
            if (myStockInfoList.isNotEmpty()) refreshAllPrices()
            calculateTopData()
        }
    }

    suspend fun addMyStock(myStockEntity: MyStockEntity): Boolean {
        return withContext(viewModelScope.coroutineContext) {
            try {
                myStockRepository.addMyStock(myStockEntity)
                myStockRepository.getAllMyStock().last {
                    myStockInfoList.add(it)
                }
                _scrollIndex.value = myStockInfoList.size
                event(Event.ShowInfoToastMessage(context.getString(R.string.MyStockFragment_Msg_MyStock_Add_Success)))
                calculateTopData()
                true
            } catch (e: Exception) {
                e.stackTrace
                event(Event.ShowErrorToastMessage(context.getString(R.string.MyStockInputDialog_Error_Message)))
                false
            }
        }
    }

    suspend fun updateMyStock(myStockEntity: MyStockEntity): Boolean =
        withContext(viewModelScope.coroutineContext) {
            try {
                myStockRepository.updateMyStock(myStockEntity)
                myStockInfoList.clear()
                myStockInfoList.addAll(myStockRepository.getAllMyStock().toMutableStateList())
                event(Event.ShowInfoToastMessage(context.getString(R.string.MyStockFragment_Msg_MyStock_Modify_Success)))
                calculateTopData()
                true
            } catch (e: Exception) {
                e.stackTrace
                event(Event.ShowErrorToastMessage(context.getString(R.string.MyStockInputDialog_Error_Message)))
                false
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
        totalGainPricePercent =
            StockUtils.calculateGainPercent(totalPurchasePrice, totalEvaluationAmount)
        viewModelScope.launch {
            _totalPurchasePrice.emit(totalPurchasePrice.toString())
            _totalEvaluationAmount.emit(totalEvaluationAmount.toString())
            _totalGainPrice.emit(totalGainPrice.toString())
            _totalGainPricePercent.emit(StockUtils.getRoundsPercentNumber(totalGainPricePercent))
        }
    }

    fun refreshAllPrices() {
        if (!NetworkUtils.isInternetAvailable(context)) {
            event(Event.ShowErrorToastMessage(context.getString(R.string.Error_Msg_Network_Connect_Exception)))
            return
        }
        viewModelScope.launch {
            repeat(myStockInfoList.size) { count ->
                val url =
                    "https://finance.naver.com/item/main.naver?code=${myStockInfoList[count].subjectCode}"
                val doc = withContext(Dispatchers.IO) {
                    try {
                        Jsoup.connect(url).get()
                    } catch (e: Exception) {
                        e.stackTrace
                        event(Event.RefreshCurrentPriceDone(false))
                        null
                    }
                }
                if (doc == null) {
                    event(Event.RefreshCurrentPriceDone(false))
                    return@launch
                }
                val blind = doc.select(".blind")
                if (blind.isNullOrEmpty()) {
                    event(Event.RefreshCurrentPriceDone(false))
                    return@launch
                }

                if (blind.size > 19) {
                    val startIndex = blind.size - 18
                    val currentPrice = blind[startIndex].text()
                    val dayToDayPrice = blind[startIndex + 1].text()
                    val dayToDayPercent = blind[startIndex + 2].text()
                    val yesterdayPrice = blind[startIndex + 3].text()

                    val currentPriceNumber = StockUtils.getNumDeletedComma(currentPrice).toInt()
                    val purchasePriceNumber =
                        StockUtils.getNumDeletedComma(myStockInfoList[count].purchasePrice).toInt()
                    val purchaseCountNumber = myStockInfoList[count].purchaseCount
                    val gainPrice = (currentPriceNumber - purchasePriceNumber) * purchaseCountNumber
                    myStockInfoList[count].apply {
                        this.currentPrice = currentPrice
                        this.dayToDayPercent = dayToDayPercent
                        this.dayToDayPrice = dayToDayPrice
                        this.yesterdayPrice = yesterdayPrice
                        this.gainPrice = StockUtils.getNumInsertComma(gainPrice.toString())
                    }
                }
                myStockRepository.updateMyStock(myStockInfoList[count])
            }
            myStockInfoList.clear()
            myStockInfoList.addAll(myStockRepository.getAllMyStock().toMutableStateList())
            calculateTopData()
            event(Event.RefreshCurrentPriceDone(true))
        }
    }

    suspend fun getCurrentPrice(subjectCode: String): MyStockEntity.CurrentPriceData {
        if (!NetworkUtils.isInternetAvailable(context)) {
            event(Event.ShowErrorToastMessage(context.getString(R.string.Error_Msg_Network_Connect_Exception)))
            return MyStockEntity.CurrentPriceData()
        }
        val currentPrice = withContext(viewModelScope.coroutineContext) {
            getCurrentPriceJob(subjectCode = subjectCode)
        }
        return currentPrice
    }

    private suspend fun getCurrentPriceJob(subjectCode: String): MyStockEntity.CurrentPriceData {
        val result = MyStockEntity.CurrentPriceData()
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
                result.apply {
                    currentPrice = blind[startIndex].text()
                    dayToDayPrice = blind[startIndex + 1].text()
                    dayToDayPercent = blind[startIndex + 2].text()
                    yesterdayPrice = blind[startIndex + 3].text()
                }
            }
        }
        return result
    }

    fun isDeleteCheck(): Boolean {
        val isDeleteCheckPref =
            preferenceRepository.getPreference(PrefKey.KEY_SETTING_MY_STOCK_SHOW_DELETE_CHECK)
        return isDeleteCheckPref == StockConfig.TRUE
    }

    fun isAutoAdd(): Boolean {
        val isAutoAddPref =
            preferenceRepository.getPreference(PrefKey.KEY_SETTING_MY_STOCK_AUTO_ADD)
        return isAutoAddPref == StockConfig.TRUE
    }

    /**
     * IncomeNote 연동
     */
    fun requestAddIncomeNote(reqIncomeNoteInfo: ReqIncomeNoteInfo, myStockEntity: MyStockEntity) {
        viewModelScope.launch {
            val result = incomeNoteRepository.addIncomeNote(reqIncomeNoteInfo)
            if (result == null) {
                event(Event.ResponseServerError(context.getString(R.string.Error_Msg_Network_Connect_Exception)))
                return@launch
            }
            if (result.isSuccessful) {
                result.data?.let { incomeNoteInfo ->
                    incomeNoteInfo.gainPercent
                    event(Event.SuccessIncomeNoteAdd(myStockEntity))
                }
            }
        }
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