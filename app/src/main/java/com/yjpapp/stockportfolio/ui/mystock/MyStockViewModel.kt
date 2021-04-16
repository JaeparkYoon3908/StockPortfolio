package com.yjpapp.stockportfolio.ui.mystock


import android.view.View
import androidx.lifecycle.MutableLiveData
import com.yjpapp.stockportfolio.base.BaseViewModel
import com.yjpapp.stockportfolio.database.room.MyStockEntity
import com.yjpapp.stockportfolio.database.sqlte.data.MyStockInfo

class MyStockViewModel(private val myStockRepository: MyStockRepository): BaseViewModel() {
    var currentPrice = MutableLiveData<String>()
    var myStockInfoList = MutableLiveData<MutableList<MyStockInfo>>()
    var position: Int = 0
    var inputDialogSubjectName = ""
    var inputDialogPurchaseDate = ""
    var inputDialogPurchasePrice = ""
    var inputDialogPurchaseCount = ""

    fun onAddButtonClick(){
        
    }

    fun onEditButtonClick(){

    }

    fun inputDialogCompleteButtonClick(){

    }

    fun onSubjectNameChange(s: CharSequence, start :Int, before : Int, count: Int){
        inputDialogSubjectName = s.toString()
    }
}