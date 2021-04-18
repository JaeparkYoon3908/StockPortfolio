package com.yjpapp.stockportfolio.ui.mystock


import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import com.yjpapp.stockportfolio.base.BaseViewModel
import com.yjpapp.stockportfolio.database.room.MyStockEntity
import com.yjpapp.stockportfolio.ui.widget.MonthYearPickerDialog
import com.yjpapp.stockportfolio.util.Event

class MyStockViewModel(private val myStockRepository: MyStockRepository) : BaseViewModel() {
    var currentPrice = MutableLiveData<String>()
    var myStockInfoList = MutableLiveData<MutableList<MyStockEntity>>()
    var position: Int = 0
    var inputDialogSubjectName = ""
    var inputDialogPurchaseDate = ""
    var inputDialogPurchasePrice = ""
    var inputDialogPurchaseCount = ""
    lateinit var mMyStockInputDialog: MyStockInputDialog
    val showErrorToast = MutableLiveData<Event<Boolean>>()
//    val showErrorToast: LiveData<Event<Boolean>> = _showErrorToast

    /**
     * MyStockFragment 영역
     */
    fun onViewCreated(){
        myStockInfoList.value = myStockRepository.getAllMyStock()
    }
    fun onAddButtonClick(myStockInputDialog: MyStockInputDialog, fragmentManager: FragmentManager) {
        mMyStockInputDialog = myStockInputDialog
        mMyStockInputDialog.apply {
            show()
            binding.etSellDate.setOnClickListener {
                var year = ""
                var month = ""
                if (binding.etSellDate.text.toString() != "") {
                    val split = binding.etSellDate.text.toString().split(".")
                    year = split[0]
                    month = split[1]
                }
                MonthYearPickerDialog(year, month).apply {
                    setListener { view, year, month, dayOfMonth ->
//                        Toast.makeText(
//                            requireContext(),
//                            "Set date: $year/$month/$dayOfMonth",
//                            Toast.LENGTH_LONG
//                        ).show()
                        uiHandler.sendEmptyMessage(MyStockInputDialog.MSG.SELL_DATE_DATA_INPUT)
                        purchaseYear = year.toString()
                        purchaseMonth = if (month < 10) {
                            "0$month"
                        } else {
                            month.toString()
                        }
                        inputDialogPurchaseDate = purchaseYear + purchaseMonth
                    }
                    show(fragmentManager, "MonthYearPickerDialog")
                }
            }
        }
    }

    fun onEditButtonClick() {

    }

    /**
     * MyStockInputDialog 영역
     */
    //종목명
    fun onSubjectNameChange(s: CharSequence, start: Int, before: Int, count: Int) {
        inputDialogSubjectName = s.toString()
    }

    //평균단가
    fun onPurchasePriceChange(s: CharSequence, start: Int, before: Int, count: Int) {
        inputDialogPurchasePrice = s.toString()
    }

    //보유수량
    fun onPurchaseCountChange(s: CharSequence, start: Int, before: Int, count: Int) {
        inputDialogPurchaseCount = s.toString()
    }

    fun inputDialogCompleteClick() {
        if (inputDialogSubjectName.isNotEmpty() &&
            inputDialogPurchasePrice.isNotEmpty() &&
            inputDialogPurchaseCount.isNotEmpty()
        ) {
            val myStockEntity = MyStockEntity(
                0, inputDialogSubjectName,
                inputDialogPurchaseDate, inputDialogPurchasePrice, inputDialogPurchaseCount
            )
            try {

            }catch (e: Exception){
                e.stackTrace
            }
            myStockRepository.insertMyStock(myStockEntity)
            myStockInfoList.value = myStockRepository.getAllMyStock()
            mMyStockInputDialog.dismiss()

        } else {
            showErrorToast.value = Event(true)
        }
    }

    fun inputDialogCancelClick() {
        mMyStockInputDialog.dismiss()
    }

    /**
     * viewModel 자체 함수 영역
     */
}