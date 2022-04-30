package com.yjpapp.stockportfolio.extension

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.databinding.BindingAdapter
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.common.StockConfig
import com.yjpapp.stockportfolio.util.StockUtils
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
    val percentNumToDouble = StockUtils.getNumDeletedPercent(percentNum).toDouble()
    if (percentNumToDouble >= 0) {
        textView.setTextColor(textView.context.getColor(R.color.color_e52b4e))
    }else{
        textView.setTextColor(textView.context.getColor(R.color.color_4876c7))
    }
}

@BindingAdapter("binding:setPrice")
fun setPrice(textView: TextView, price: Double) {
    val moneySymbol = Currency.getInstance(Locale.KOREA).symbol
    textView.text = moneySymbol + StockUtils.getNumInsertComma(BigDecimal(price).toString())
}

@BindingAdapter("android:text")
fun setText(textView: TextView, text: Double) {
    textView.text = text.toString()
}

@BindingAdapter("android:text")
fun setText(textView: TextView, text: Int) {
    textView.text = text.toString()
}

@BindingAdapter("binding:visibility")
fun visibility(view: View, visibility: Boolean) {
    if (visibility) {
        view.visibility = View.VISIBLE
    } else {
        view.visibility = View.GONE
    }
}
/**
 * IncomeNote
 */
@BindingAdapter("binding:addPercentText")
fun addPercentText(textView: TextView, number: Double) {
    textView.text = "(${StockUtils.getRoundsPercentNumber(number)})"
}

/**
 * LoginActivity
 */
@BindingAdapter("binding:setLoginTypeImage")
fun setLoginTypeImage(imageView: ImageView, loginType: String) {
    when (loginType) {
        StockConfig.LOGIN_TYPE_FACEBOOK -> {
            imageView.setBackgroundResource(R.drawable.ic_facebook)
        }
        StockConfig.LOGIN_TYPE_GOOGLE -> {
            imageView.setBackgroundResource(R.drawable.ic_google)
        }
        StockConfig.LOGIN_TYPE_NAVER -> {
            imageView.setBackgroundResource(R.drawable.ic_naver)
        }
    }
}

/**
 * MyFragment
 */
@BindingAdapter("binding:switchChecked")
fun switchChecked(switch: SwitchCompat, isChecked: String) {
    when (isChecked) {
        "true" -> {
            switch.isChecked = true
        }
        "false" -> {
            switch.isChecked = false
        }
    }
}
