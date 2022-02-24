package com.yjpapp.stockportfolio.function.mystock


import android.content.Context
import androidx.compose.runtime.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.extension.MutableEventFlow
import com.yjpapp.stockportfolio.extension.asEventFlow
import com.yjpapp.stockportfolio.function.mystock.dialog.MyStockInputDialog
import com.yjpapp.stockportfolio.localdb.room.mystock.MyStockEntity
import com.yjpapp.stockportfolio.repository.MyStockRepository
import com.yjpapp.stockportfolio.test.model.LatestNewsUiState
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

    val totalPurchasePrice = MutableLiveData<String>() //상단 총 매수금액
    val totalEvaluationAmount = MutableLiveData<String>() //상단 총 평가금액
    val totalGainPrice = MutableLiveData<String>() //상단 손익
    val totalGainPricePercent = MutableLiveData<String>() //상단 수익률
    var myStockInfoList = mutableStateListOf<MyStockEntity>() //나의 주식 목록 List
        private set
    private val _scrollIndex by lazy { MutableStateFlow(myStockInfoList.size) }
    val scrollIndex: StateFlow<Int> get() = _scrollIndex
//    private val _scrollIndex by lazy { MutableLiveData(myStockInfoList.size) }
//    val scrollIndex:LiveData<Int> get() = _scrollIndex
//    val showErrorToast = MutableLiveData<Event<Boolean>>() //필수 값을 모두 입력해주세요 Toast
//    val showDBSaveErrorToast = MutableLiveData<Event<Boolean>>() //DB가 에러나서 저장 안된다는 Toast

    /**
     * MyStockFragment 영역
     */
    init {
        myStockInfoList = myStockRepository.getAllMyStock().toMutableStateList()
        event(Event.SendMyStockInfoList(myStockInfoList))
    }

    //확인버튼 클릭 후 Save
    fun saveMyStock(context: Context, myStockEntity: MyStockEntity): Boolean {
        try {
            if (myStockEntity.id == 0) {
                myStockRepository.insertMyStock(myStockEntity)
                myStockInfoList.add(myStockEntity)
                _scrollIndex.value = myStockInfoList.size - 1
            } else {
                myStockRepository.updateMyStock(myStockEntity)
                myStockInfoList.clear()
                myStockInfoList.addAll(myStockRepository.getAllMyStock().toMutableStateList())
            }
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
            true
        } catch (e: Exception) {
            e.stackTrace
            false
        }
    }

    private fun event(event: Event) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }

    sealed class Event {
        data class ShowErrorToastMessage(val msg: String): Event()
        data class SendMyStockInfoList(val myStockInfoList: MutableList<MyStockEntity>): Event()
    }
}