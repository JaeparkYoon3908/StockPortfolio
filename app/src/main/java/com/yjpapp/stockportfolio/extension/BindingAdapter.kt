package com.yjpapp.stockportfolio.extension

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("bind:isSelected")
fun setSelected(view: View, selected: Boolean) {
    view.isSelected = selected
}