package com.yjpapp.stockportfolio.ui.view

import android.view.View

interface MainView {
    fun addFragment()

    fun showMyStock()

    fun hideMyStock()

    fun showIncomeNote()

    fun hideIncomeNote()

    fun showMemoList()

    fun hideMemoList()

    fun hideCurrentFragment()

    fun clickBottomMenu(view: View?)
}