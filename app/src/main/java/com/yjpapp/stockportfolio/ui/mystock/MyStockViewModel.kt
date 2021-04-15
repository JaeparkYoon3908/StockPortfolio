package com.yjpapp.stockportfolio.ui.mystock

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yjpapp.stockportfolio.base.BaseViewModel

class MyStockViewModel: BaseViewModel() {
    var currentPrice = MutableLiveData<String>()


}