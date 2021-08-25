package com.yjpapp.stockportfolio.function.incomenote

import android.widget.Toast
import com.yjpapp.stockportfolio.model.response.RespIncomeNoteInfo

/**
 * IncomeNoteFragment의 View
 *
 * @author Yoon Jae-park
 * @since 2020.12
 */
interface IncomeNoteView {
    fun bindTotalGainData()

    fun showAddButton()

    fun showFilterDialog()

    fun showInputDialog(editMode: Boolean, respIncomeNoteInfo: RespIncomeNoteInfo.IncomeNoteList?)

    fun hideAddButton()

    fun scrollTopPosition(topPosition: Int)

    fun setAdapter(incomeNoteListAdapter: IncomeNoteListAdapter?)

    fun showToast(toast: Toast)

    fun initFilterDateText(startDate: String, endDate: String)
}