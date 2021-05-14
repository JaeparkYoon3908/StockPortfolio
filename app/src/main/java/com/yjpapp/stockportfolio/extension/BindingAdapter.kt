package com.yjpapp.stockportfolio.extension

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.util.Utils

@BindingAdapter("bind:isSelected")
fun setSelected(view: View, selected: Boolean) {
    view.isSelected = selected
}

@BindingAdapter("bind:setNumColor")
fun setNumColor(textView: TextView, num: String) {
    val numToDouble = num.toDouble()
    if (numToDouble > 0) {
        textView.setTextColor(textView.context.getColor(R.color.color_e52b4e))
    } else {
        textView.setTextColor(textView.context.getColor(R.color.color_4876c7))
    }
}

@BindingAdapter("bind:setPercentColor")
fun setPercentNumColor(textView: TextView, percentNum: String) {
    val percentNumToDouble = Utils.getNumDeletedPercent(percentNum).toDouble()
    if (percentNumToDouble > 0) {
        textView.setTextColor(textView.context.getColor(R.color.color_e52b4e))
    }else{
        textView.setTextColor(textView.context.getColor(R.color.color_4876c7))
    }
}