package com.yjpapp.stockportfolio.util

import android.view.View
import android.widget.ImageView
import android.widget.Switch
import androidx.appcompat.widget.SwitchCompat
import androidx.databinding.BindingAdapter
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.constance.StockPortfolioConfig

@BindingAdapter("android:isSelected")
fun isSelected(view: View, isSelected: Boolean) {
    view.isSelected = isSelected
}

@BindingAdapter("binding:setLoginTypeImage")
fun setLoginTypeImage(imageView: ImageView, loginType: String) {
    when (loginType) {
        StockPortfolioConfig.LOGIN_TYPE_FACEBOOK -> {
            imageView.setBackgroundResource(R.drawable.ic_facebook)
        }
        StockPortfolioConfig.LOGIN_TYPE_GOOGLE -> {
            imageView.setBackgroundResource(R.drawable.ic_google)
        }
        StockPortfolioConfig.LOGIN_TYPE_NAVER -> {
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

