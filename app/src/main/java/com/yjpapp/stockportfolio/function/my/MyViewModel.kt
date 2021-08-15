package com.yjpapp.stockportfolio.function.my

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel(context: Context): ViewModel() {
    var phoneNum = MutableLiveData<String>()

    fun onTextChanged(s: CharSequence, start :Int, before : Int, count: Int){
        phoneNum.postValue(s.toString())
    }

    fun sendButtonClicked(){

    }
}