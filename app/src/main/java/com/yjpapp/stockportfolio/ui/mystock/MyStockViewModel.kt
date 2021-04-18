package com.yjpapp.stockportfolio.ui.mystock


import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import com.yjpapp.stockportfolio.base.BaseViewModel
import com.yjpapp.stockportfolio.database.room.MyStockEntity
import com.yjpapp.stockportfolio.database.sqlte.data.MyStockInfo
import com.yjpapp.stockportfolio.ui.incomenote.IncomeNoteInputDialog
import com.yjpapp.stockportfolio.ui.widget.MonthYearPickerDialog
import com.yjpapp.stockportfolio.util.Utils

class MyStockViewModel(private val myStockRepository: MyStockRepository): BaseViewModel() {
    var currentPrice = MutableLiveData<String>()
    var myStockInfoList = MutableLiveData<MutableList<MyStockInfo>>()
    var position: Int = 0
    var inputDialogSubjectName = ""
    var inputDialogPurchaseDate = ""
    var inputDialogPurchasePrice = ""
    var inputDialogPurchaseCount = ""
    lateinit var mMyStockInputDialog: MyStockInputDialog

    /**
     * MyStockFragment 영역
     */
    fun onAddButtonClick(myStockInputDialog: MyStockInputDialog, fragmentManager: FragmentManager){
        mMyStockInputDialog = myStockInputDialog
        myStockInputDialog.apply {
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
                        Toast.makeText(requireContext(), "Set date: $year/$month/$dayOfMonth", Toast.LENGTH_LONG).show()
                        uiHandler.sendEmptyMessage(MyStockInputDialog.MSG.SELL_DATE_DATA_INPUT)
                        purchaseYear = year.toString()
                        purchaseMonth = if (month < 10) {
                            "0$month"
                        } else {
                            month.toString()
                        }
                    }
                    show(fragmentManager, "MonthYearPickerDialog")
                }
            }
        }
    }

    fun onEditButtonClick(){

    }

    fun onSubjectNameChange(s: CharSequence, start :Int, before : Int, count: Int){
        inputDialogSubjectName = s.toString()
    }

    /**
     * MyStockInputDialog 영역
     */
    fun inputDialogCompleteClick(){

    }

    fun inputDialogCancelClick(){
        mMyStockInputDialog.dismiss()
    }

    /**
     * viewModel 자체 함수 영역
     */

    private fun showInputDialog(){
        
    }
}