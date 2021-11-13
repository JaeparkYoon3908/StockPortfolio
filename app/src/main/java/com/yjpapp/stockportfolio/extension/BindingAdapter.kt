package com.yjpapp.stockportfolio.extension

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.util.Utils
import java.math.BigDecimal
import java.util.*

/**
 * Global
 */
@BindingAdapter("binding:isSelected")
fun isSelected(view: View, selected: Boolean) {
    view.isSelected = selected
}

@BindingAdapter("binding:setNumColor")
fun setNumColor(textView: TextView, num: String) {
    val numToDouble = num.toDouble()
    if (numToDouble >= 0) {
        textView.setTextColor(textView.context.getColor(R.color.color_e52b4e))
    } else {
        textView.setTextColor(textView.context.getColor(R.color.color_4876c7))
    }
}

@BindingAdapter("binding:setNumColor")
fun setNumColor(textView: TextView, numToDouble: Double) {
    if (numToDouble >= 0) {
        textView.setTextColor(textView.context.getColor(R.color.color_e52b4e))
    } else {
        textView.setTextColor(textView.context.getColor(R.color.color_4876c7))
    }
}

@BindingAdapter("binding:setPercentColor")
fun setPercentNumColor(textView: TextView, percentNum: String) {
    val percentNumToDouble = Utils.getNumDeletedPercent(percentNum).toDouble()
    if (percentNumToDouble >= 0) {
        textView.setTextColor(textView.context.getColor(R.color.color_e52b4e))
    }else{
        textView.setTextColor(textView.context.getColor(R.color.color_4876c7))
    }
}

@BindingAdapter("binding:setPrice")
fun setPrice(textView: TextView, price: Double) {
    val moneySymbol = Currency.getInstance(Locale.KOREA).symbol
    textView.text = moneySymbol + Utils.getNumInsertComma(BigDecimal(price).toString())
}

@BindingAdapter("android:text")
fun setText(textView: TextView, text: Double) {
    textView.text = text.toString()
}

@BindingAdapter("android:text")
fun setText(textView: TextView, text: Int) {
    textView.text = text.toString()
}
/**
 * IncomeNote
 */
@BindingAdapter("binding:addPercentText")
fun addPercentText(textView: TextView, number: Double) {
    val roundNum = Utils.getRoundNum(number, 2)
    textView.text = "($roundNum%)"
}