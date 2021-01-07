package com.yjpapp.stockportfolio.ui.view

import com.yjpapp.stockportfolio.database.data.IncomeNoteInfo

interface IncomeNoteView {
    fun bindTotalGainData()
    fun showAllData(allDataList: ArrayList<IncomeNoteInfo?>)
    fun showGainData(gainDataList: ArrayList<IncomeNoteInfo?>)
    fun showLossData(lossDataList: ArrayList<IncomeNoteInfo?>)
    fun showAddButton()
    fun showFilterDialog()
    fun showInputDialog(editMode: Boolean, id: Int, incomeNoteInfo: IncomeNoteInfo?)
    fun hideAddButton()
    fun addIncomeNoteData(newDataList: ArrayList<IncomeNoteInfo?>)
    fun updateIncomeNoteData(newDataList: ArrayList<IncomeNoteInfo?>)
    fun deleteIncomeNoteData()
}