package com.yjpapp.stockportfolio.ui.mystock


import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.base.BaseViewModel
import com.yjpapp.stockportfolio.database.room.MyStockEntity
import com.yjpapp.stockportfolio.util.Event
import java.text.DecimalFormat

class MyStockViewModel(private val myStockRepository: MyStockRepository) : BaseViewModel() {
    val NOTIFY_HANDLER_INSERT = "NOTIFY_INSERT"
    val NOTIFY_HANDLER_DELETE = "NOTIFY_DELETE"
    val NOTIFY_HANDLER_UPDATE = "NOTIFY_UPDATE"

    var currentPrice = MutableLiveData<String>()
    var myStockInfoList = MutableLiveData<MutableList<MyStockEntity>>()
    var inputDialogSubjectName = ""
    var inputDialogPurchaseDate = ""
    var inputDialogPurchasePrice = MutableLiveData<String>()

    var inputDialogPurchaseCount = ""
    lateinit var mMyStockInputDialog: MyStockInputDialog
    val showErrorToast = MutableLiveData<Event<Boolean>>()
    val showDBSaveErrorToast = MutableLiveData<Event<Boolean>>()
    var notifyHandler = "Delete"

    var purchasePriceTextColorRes = MutableLiveData<Int>()
    lateinit var inputDialogController: MyStockInputDialogController

    /**
     * MyStockFragment 영역
     */
    fun onViewCreated(){
        myStockInfoList.value = myStockRepository.getAllMyStock()
    }

    /**
     * MyStockInputDialog 영역
     */
    //3자리마다 콤마 찍어주는 변수
    private val decimalFormat = DecimalFormat("###,###")
    private var convertText = ""

    //종목명
    fun onSubjectNameChange(s: CharSequence, start: Int, before: Int, count: Int) {
        inputDialogSubjectName = s.toString()
    }

    //평균단가
    fun onPurchasePriceChange(s: CharSequence, start: Int, before: Int, count: Int) {
        if(!TextUtils.isEmpty(s.toString()) && s.toString() != convertText){
            convertText = decimalFormat.format(s.toString().replace(",", "").toDouble())
            inputDialogPurchasePrice.value = convertText
        }

        if(s.isEmpty()){
            inputDialogController.changeMoneySymbolTextColor(R.color.color_666666)
        }else{
            inputDialogController.changeMoneySymbolTextColor(R.color.color_222222)
        }
    }

    //보유수량
    fun onPurchaseCountChange(s: CharSequence, start: Int, before: Int, count: Int) {
        inputDialogPurchaseCount = s.toString()
    }

    //확인버튼 클릭 후 Save
    fun saveMyStock(isInsertMode: Boolean, id: Int): Boolean {
        if (inputDialogSubjectName.isNotEmpty() &&
            inputDialogPurchasePrice.value?.length!=0 &&
            inputDialogPurchaseCount.isNotEmpty()
        ) {
            try {
                val myStockEntity = MyStockEntity(
                    id, inputDialogSubjectName,
                    inputDialogPurchaseDate, inputDialogPurchasePrice.value!!, inputDialogPurchaseCount
                )
                if(isInsertMode){
                    myStockRepository.insertMyStock(myStockEntity)
                    notifyHandler = NOTIFY_HANDLER_INSERT
                }else{
                    myStockRepository.updateMyStock(myStockEntity)
                    notifyHandler = NOTIFY_HANDLER_UPDATE
                }
                myStockInfoList.value = myStockRepository.getAllMyStock()
                return true
            }catch (e: Exception){
                e.stackTrace
                showDBSaveErrorToast.value = Event(true)
                return false
            }
        } else {
            showErrorToast.value = Event(true)
            return false
        }
    }

    fun deleteMyStock(myStockEntity: MyStockEntity){
        myStockRepository.deleteMyStock((myStockEntity))
        notifyHandler = NOTIFY_HANDLER_DELETE
        myStockInfoList.value = myStockRepository.getAllMyStock()
    }

    /**
     * viewModel 자체 함수 영역
     */
}