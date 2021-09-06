package com.yjpapp.stockportfolio.function.main

import android.view.View
import androidx.fragment.app.Fragment

/**
 * MainActivity의 View
 *
 * @author Yoon Jae-park
 * @since 2020.12
 */

interface MainView {

    fun showFragment(fragment: Fragment)

    fun clickBottomMenu(view: View?)
}