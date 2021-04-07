package com.yjpapp.stockportfolio.ui.main

import android.view.View

/**
 * MainActivity의 View
 *
 * @author Yoon Jae-park
 * @since 2020.12
 */

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