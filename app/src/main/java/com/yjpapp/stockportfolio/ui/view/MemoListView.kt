package com.yjpapp.stockportfolio.ui.view

import android.content.Intent
import com.yjpapp.stockportfolio.ui.adapter.MemoListAdapter

interface MemoListView {
    fun startReadWriteActivityForResult(intent: Intent, requestCode: Int)

    fun showAddButton()

    fun hideAddButton()

    fun showDeleteButton()

    fun hideDeleteButton()

    fun showGuideMessage()

    fun hideGuideMessage()

    fun showDeleteCheckDialog()

    fun setAdapter(memoListAdapter: MemoListAdapter)
}