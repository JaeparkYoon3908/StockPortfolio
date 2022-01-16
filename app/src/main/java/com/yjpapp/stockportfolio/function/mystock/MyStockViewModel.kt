package com.yjpapp.stockportfolio.function.mystock


import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.function.mystock.dialog.MyStockInputDialogController
import com.yjpapp.stockportfolio.localdb.room.mystock.MyStockEntity
import com.yjpapp.stockportfolio.repository.MyStockRepository
import com.yjpapp.stockportfolio.util.Event
import com.yjpapp.stockportfolio.util.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyStockViewModel @Inject constructor(
    private val myStockRepository: MyStockRepository
) : ViewModel() {
    val NOTIFY_HANDLER_INSERT = "NOTIFY_INSERT"
    val NOTIFY_HANDLER_DELETE = "NOTIFY_DELETE"
    val NOTIFY_HANDLER_UPDATE = "NOTIFY_UPDATE"

    val totalPurchasePrice = MutableLiveData<String>() //상단 총 매수금액
    val totalEvaluationAmount = MutableLiveData<String>() //상단 총 평가금액
    val totalGainPrice = MutableLiveData<String>() //상단 손익
    val totalGainPricePercent = MutableLiveData<String>() //상단 수익률

    var currentPrice = MutableLiveData<String>()
    val myStockInfoList = MutableLiveData<MutableList<MyStockEntity>>() //나의 주식 목록 List
    var inputDialogSubjectName = "" //InputDialog 회사명
    var inputDialogPurchaseDate = "" //InputDialog 매수일
    val inputDialogPurchasePrice = MutableLiveData<String>() //InputDialog 평균단가
    var inputDialogPurchaseCount = "" //보유수량

    val showErrorToast = MutableLiveData<Event<Boolean>>() //필수 값을 모두 입력해주세요 Toast
    val showDBSaveErrorToast = MutableLiveData<Event<Boolean>>() //DB가 에러나서 저장 안된다는 Toast
    var notifyHandler = NOTIFY_HANDLER_INSERT //RecyclerView adapter notify handler

    lateinit var inputDialogController: MyStockInputDialogController //InputDialog와 연결 된 interface

    /**
     * MyStockFragment 영역
     */
    fun onViewCreated() {
        myStockInfoList.value = myStockRepository.getAllMyStock()
    }

    /**
     * MyStockInputDialog 영역
     */ //3자리마다 콤마 찍어주는 변수
    private var convertText = ""

    //종목명
    fun onSubjectNameChange(s: CharSequence, start: Int, before: Int, count: Int) {
        inputDialogSubjectName = s.toString()
    }

    //평균단가
    fun onPurchasePriceChange(s: CharSequence, start: Int, before: Int, count: Int) {
        if (!TextUtils.isEmpty(s.toString()) && s.toString() != convertText) {
            convertText = Utils.getNumInsertComma(s.toString())
            inputDialogPurchasePrice.value = convertText
        }

        if (s.isEmpty()) {
            inputDialogController.changeMoneySymbolTextColor(R.color.color_666666)
        } else {
            inputDialogController.changeMoneySymbolTextColor(R.color.color_222222)
        }
    }

    //보유수량
    fun onPurchaseCountChange(s: CharSequence, start: Int, before: Int, count: Int) {
        inputDialogPurchaseCount = s.toString()
    }

    //확인버튼 클릭 후 Save
    fun saveMyStock(isInsertMode: Boolean, id: Int): Boolean {
        if (inputDialogSubjectName.isNotEmpty() && inputDialogPurchasePrice.value?.length != 0 && inputDialogPurchaseCount.isNotEmpty()) {
            try {
                val myStockEntity = MyStockEntity(
                    id,
                    inputDialogSubjectName,
                    inputDialogPurchaseDate,
                    inputDialogPurchasePrice.value!!,
                    inputDialogPurchaseCount
                )
                if (isInsertMode) {
                    myStockRepository.insertMyStock(myStockEntity)
                    notifyHandler = NOTIFY_HANDLER_INSERT
                } else {
                    myStockRepository.updateMyStock(myStockEntity)
                    notifyHandler = NOTIFY_HANDLER_UPDATE
                }
                myStockInfoList.value = myStockRepository.getAllMyStock()
                return true
            } catch (e: Exception) {
                e.stackTrace
                showDBSaveErrorToast.value = Event(true)
                return false
            }
        } else {
            showErrorToast.value = Event(true)
            return false
        }
    }

    fun deleteMyStock(myStockEntity: MyStockEntity) {
        myStockRepository.deleteMyStock((myStockEntity))
        notifyHandler = NOTIFY_HANDLER_DELETE
        myStockInfoList.value = myStockRepository.getAllMyStock()
    }

    /**
     * viewModel 자체 함수 영역
     */

}