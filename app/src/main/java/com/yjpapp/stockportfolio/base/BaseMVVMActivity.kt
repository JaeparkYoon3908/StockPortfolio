package com.yjpapp.stockportfolio.base

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

open class BaseMVVMActivity: AppCompatActivity() {
    protected inline fun <reified T : ViewDataBinding> binding(@LayoutRes resId: Int): Lazy<T> =
        lazy { DataBindingUtil.setContentView<T>(this, resId) }

//    protected fun binding(@LayoutRes resId: Int): Lazy<ViewDataBinding> =
//        lazy { DataBindingUtil.setContentView(this, resId) }
}