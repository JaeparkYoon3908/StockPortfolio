package com.yjpapp.stockportfolio.ui.advertisement

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AdViewModel(context: Context): ViewModel() {
    var phoneNum = MutableLiveData<String>()

    fun onTextChanged(s: CharSequence, start :Int, before : Int, count: Int){
        phoneNum.postValue(s.toString())
    }

    fun sendButtonClicked(){

    }
}