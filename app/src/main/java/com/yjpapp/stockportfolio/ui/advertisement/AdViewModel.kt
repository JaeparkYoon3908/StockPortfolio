package com.yjpapp.stockportfolio.ui.advertisement

import androidx.lifecycle.MutableLiveData
import com.yjpapp.stockportfolio.base.BaseViewModel

class AdViewModel: BaseViewModel() {
    var phoneNum = MutableLiveData<String>()

    fun onTextChanged(s: CharSequence, start :Int, before : Int, count: Int){
        phoneNum.postValue(s.toString())
    }

    fun sendButtonClicked(){

    }
}