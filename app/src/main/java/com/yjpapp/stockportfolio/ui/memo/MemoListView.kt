package com.yjpapp.stockportfolio.ui.memo

import android.content.Intent

/**
 * MemoListFragment의 View
 *
 * @author Yoon Jae-park
 * @since 2020.12
 */
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