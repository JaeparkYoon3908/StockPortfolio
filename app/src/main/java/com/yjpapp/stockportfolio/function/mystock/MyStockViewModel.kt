package com.yjpapp.stockportfolio.function.mystock


import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.extension.MutableEventFlow
import com.yjpapp.stockportfolio.extension.asEventFlow
import com.yjpapp.stockportfolio.function.mystock.dialog.MyStockInputDialog
import com.yjpapp.stockportfolio.localdb.room.mystock.MyStockEntity
import com.yjpapp.stockportfolio.repository.MyStockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyStockViewModel @Inject constructor(
    private val myStockRepository: MyStockRepository
) : ViewModel() {
    private val _eventFlow = MutableEventFlow<Event>()
    val eventFlow = _eventFlow.asEventFlow()

    val NOTIFY_HANDLER_INSERT = "NOTIFY_INSERT"
    val NOTIFY_HANDLER_DELETE = "NOTIFY_DELETE"
    val NOTIFY_HANDLER_UPDATE = "NOTIFY_UPDATE"

    val totalPurchasePrice = MutableLiveData<String>() //상단 총 매수금액
    val totalEvaluationAmount = MutableLiveData<String>() //상단 총 평가금액
    val totalGainPrice = MutableLiveData<String>() //상단 손익
    val totalGainPricePercent = MutableLiveData<String>() //상단 수익률

//    var currentPrice = MutableLiveData<String>()
//    val myStockInfoList = MutableLiveData<MutableList<MyStockEntity>>() //나의 주식 목록 List
//    var inputDialogSubjectName = "" //InputDialog 회사명
//    var inputDialogPurchaseDate = "" //InputDialog 매수일
//    var inputDialogPurchasePrice = "" //InputDialog 평균단가
//    var inputDialogPurchaseCount = "" //보유수량

//    val showErrorToast = MutableLiveData<Event<Boolean>>() //필수 값을 모두 입력해주세요 Toast
//    val showDBSaveErrorToast = MutableLiveData<Event<Boolean>>() //DB가 에러나서 저장 안된다는 Toast
    var notifyHandler = NOTIFY_HANDLER_INSERT //RecyclerView adapter notify handler

    /**
     * MyStockFragment 영역
     */
    init {
        val myStockInfoList = myStockRepository.getAllMyStock()
        event(Event.SendMyStockInfoList(myStockInfoList))
    }

    //확인버튼 클릭 후 Save
    fun saveMyStock(
        context: Context,
        isInsertMode: Boolean,
        id: Int,
        myStockInputDialogData: MyStockInputDialog.MyStockInputDialogData
    ): Boolean {
        try {
            val myStockEntity = MyStockEntity(
                id,
                myStockInputDialogData.subjectName,
                myStockInputDialogData.purchaseDate,
                myStockInputDialogData.purchasePrice,
                myStockInputDialogData.purchaseCount
            )
            notifyHandler = if (isInsertMode) {
                myStockRepository.insertMyStock(myStockEntity)
                NOTIFY_HANDLER_INSERT
            } else {
                myStockRepository.updateMyStock(myStockEntity)
                NOTIFY_HANDLER_UPDATE
            }
//            myStockInfoList.value = myStockRepository.getAllMyStock()
            event(Event.SendMyStockInfoList(myStockRepository.getAllMyStock()))
            return true
        } catch (e: Exception) {
            e.stackTrace
            event(Event.ShowErrorToastMessage(context.getString(R.string.MyStockInputDialog_Error_Message)))
            return false
        }
    }

    fun deleteMyStock(myStockEntity: MyStockEntity) {
        myStockRepository.deleteMyStock((myStockEntity))
        notifyHandler = NOTIFY_HANDLER_DELETE
        val myStockInfoList = myStockRepository.getAllMyStock()
        event(Event.SendMyStockInfoList(myStockInfoList))
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