package com.yjpapp.stockportfolio.function.main

import android.view.View

/**
 * MainActivity의 View
 *
 * @author Yoon Jae-park
 * @since 2020.12
 */

interface MainView {
//    fun addFragment()

    fun showMyStock()

//    fun hideMyStock()

    fun showIncomeNote()

//    fun hideIncomeNote()

    fun showMemoList()

//    fun hideMemoList()

    fun showMyFragment()

    fun hideMyFragment()

    fun hideCurrentFragment()

    fun clickBottomMenu(view: View?)
}