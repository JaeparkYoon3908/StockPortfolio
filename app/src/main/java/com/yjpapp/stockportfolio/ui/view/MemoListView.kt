package com.yjpapp.stockportfolio.ui.view

import com.yjpapp.stockportfolio.database.data.MemoInfo

interface MemoListView {
    fun startMemoReadWriteActivity()

    fun refreshMemoListData(updateMemoList: ArrayList<MemoInfo?>)

    fun setDeleteModeOff()

    fun showAddButton()

    fun hideAddButton()

    fun showDeleteButton()

    fun hideDeleteButton()

    fun showGuideMessage()

    fun hideGuideMessage()

    fun addMemoListView(updateMemoList: ArrayList<MemoInfo?>)

    fun updateMemoListView(updateMemoList: ArrayList<MemoInfo?>)

    fun deleteMemoListView(updateMemoList: ArrayList<MemoInfo?>, position: Int)
}