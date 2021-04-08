package com.yjpapp.stockportfolio.ui.smsauth

import androidx.lifecycle.MutableLiveData
import com.yjpapp.stockportfolio.base.BaseViewModel

class SMSAuthViewModel: BaseViewModel() {

    var phoneNum = MutableLiveData<String>()

    fun onTextChanged(s: CharSequence, start :Int, before : Int, count: Int){
        phoneNum.postValue(s.toString())
    }

    fun sendButtonClicked(){

    }
}