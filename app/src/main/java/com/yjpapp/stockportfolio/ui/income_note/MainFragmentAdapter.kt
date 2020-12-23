package com.yjpapp.stockportfolio.ui.income_note

import android.view.View
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter

class MainFragmentAdapter: PagerAdapter() {
    private val incomeNoteFragment = IncomeNoteFragment()

    @NonNull
    fun getItem(position: Int): Fragment{
        when(position){
            1 -> return incomeNoteFragment
            else -> return incomeNoteFragment

        }
    }


    override fun getCount(): Int {
        return 0
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view==`object`
    }

}