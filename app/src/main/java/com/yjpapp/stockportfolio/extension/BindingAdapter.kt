package com.yjpapp.stockportfolio.extension

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.yjpapp.stockportfolio.R

@BindingAdapter("bind:isSelected")
fun setSelected(view: View, selected: Boolean) {
    view.isSelected = selected
}

@BindingAdapter("bind:setColor")
fun setNumColor(textView: TextView, num: Double){
    if(num>0){
       textView.setTextColor(textView.context.getColor(R.color.color_e52b4e))
    }else{
        textView.setTextColor(textView.context.getColor(R.color.color_4876c7))
    }
}

@BindingAdapter("bind:setPercentColor")
fun setPercentNumColor(textView: TextView, percentNum: String){
    //TODO percentNum을 숫자로 변환해서 양수 음수 판단.
}