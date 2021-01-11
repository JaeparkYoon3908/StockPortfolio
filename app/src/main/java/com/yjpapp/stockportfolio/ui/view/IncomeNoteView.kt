package com.yjpapp.stockportfolio.ui.view

import com.yjpapp.stockportfolio.database.data.IncomeNoteInfo
import com.yjpapp.stockportfolio.ui.adapter.IncomeNoteListAdapter

interface IncomeNoteView {
    fun bindTotalGainData()

    fun changeFilterText(text: String)

//    fun showGainData(gainDataList: ArrayList<IncomeNoteInfo?>)
//
//    fun showLossData(lossDataList: ArrayList<IncomeNoteInfo?>)

    fun showAddButton()

    fun showFilterDialog()

    fun showInputDialog(editMode: Boolean, incomeNoteInfo: IncomeNoteInfo?)

    fun hideAddButton()

//    fun addIncomeNoteView(newDataList: ArrayList<IncomeNoteInfo?>)

//    fun updateIncomeNoteView(newDataList: ArrayList<IncomeNoteInfo?>)

//    fun deleteIncomeNoteView(newDataList: ArrayList<IncomeNoteInfo?>)

    fun scrollTopPosition(topPosition: Int)

    fun setAdapter(incomeNoteListAdapter: IncomeNoteListAdapter)

}