package com.yjpapp.stockportfolio.ui.view

import com.yjpapp.stockportfolio.database.data.MyStockInfo
import com.yjpapp.stockportfolio.ui.adapter.MyStockListAdapter

interface MyStockView {
    fun addButtonClick()

    fun showInputDialog(editMode: Boolean, myStockInfo: MyStockInfo?)

    fun showAddButton()

    fun hideAddButton()

    fun showEditButton()

    fun hideEditButton()

    fun setAdapter(myStockListAdapter: MyStockListAdapter)

    fun scrollTopPosition(topPosition: Int)

    fun bindTotalGainData()

    fun changeFilterText(text: String)
}