package com.yjpapp.stockportfolio.ui.view

import com.yjpapp.stockportfolio.database.data.IncomeNoteInfo

interface IncomeNoteView {
    fun refreshTotalGain()
    fun showAllData(allDataList: ArrayList<IncomeNoteInfo?>)
    fun showGainData(gainDataList: ArrayList<IncomeNoteInfo?>)
    fun showLossData(lossDataList: ArrayList<IncomeNoteInfo?>)
    fun showAddButton()
    fun showFilterDialog()
    fun showEditDialog(editMode: Boolean, position: Int)
    fun hideAddButton()
    fun addIncomeNote()
    fun deleteIncomeNote()
}