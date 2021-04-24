package com.yjpapp.stockportfolio.ui.mystock


import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.base.BaseViewModel
import com.yjpapp.stockportfolio.database.room.MyStockEntity
import com.yjpapp.stockportfolio.util.Event
import java.text.DecimalFormat

class MyStockViewModel(private val myStockRepository: MyStockRepository) : BaseViewModel() {

    var currentPrice = MutableLiveData<String>()
    var myStockInfoList = MutableLiveData<MutableList<MyStockEntity>>()
    var position = 0
    var inputDialogSubjectName = ""
    var inputDialogPurchaseDate = ""
    var inputDialogPurchasePrice = ""
    var inputDialogPurchaseCount = ""
    lateinit var mMyStockInputDialog: MyStockInputDialog
    val showErrorToast = MutableLiveData<Event<Boolean>>()
    val showDBSaveErrorToast = MutableLiveData<Event<Boolean>>()

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
    private var result = "";
    //종목명
    fun onSubjectNameChange(s: CharSequence, start: Int, before: Int, count: Int) {
        inputDialogSubjectName = s.toString()
    }

    //평균단가
    fun onPurchasePriceChange(charSequence: CharSequence, start: Int, before: Int, count: Int) {
//        inputDialogPurchasePrice = charSequence.toString()
        if(!TextUtils.isEmpty(charSequence.toString()) && charSequence.toString() != result){
            inputDialogPurchasePrice = decimalFormat.format(charSequence.toString().replace(",", "").toDouble())
        }

        if(charSequence.isEmpty()){
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
    fun saveMyStock(): Boolean {
        if (inputDialogSubjectName.isNotEmpty() &&
            inputDialogPurchasePrice.isNotEmpty() &&
            inputDialogPurchaseCount.isNotEmpty()
        ) {
            try {
                val myStockEntity = MyStockEntity(
                    0, inputDialogSubjectName,
                    inputDialogPurchaseDate, inputDialogPurchasePrice, inputDialogPurchaseCount
                )
                myStockRepository.insertMyStock(myStockEntity)
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

    fun deleteMyStock(myStockEntity: MyStockEntity): MutableList<MyStockEntity>{
        myStockRepository.deleteMyStock((myStockEntity))
        return myStockRepository.getAllMyStock()
    }

    /**
     * viewModel 자체 함수 영역
     */
}