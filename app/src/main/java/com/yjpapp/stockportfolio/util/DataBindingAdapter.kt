package com.yjpapp.stockportfolio.util

import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.SwitchCompat
import androidx.databinding.BindingAdapter
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.constance.StockConfig

@BindingAdapter("android:isSelected")
fun isSelected(view: View, isSelected: Boolean) {
    view.isSelected = isSelected
}

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
            imageView.setBackgroundResource(R.drawable.naver_icon)
        }
    }
}
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

