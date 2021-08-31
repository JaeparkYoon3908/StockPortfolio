package com.yjpapp.stockportfolio.util

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("android:isSelected")
fun isSelected(view: View, isSelected: Boolean) {
    view.isSelected = isSelected
}
