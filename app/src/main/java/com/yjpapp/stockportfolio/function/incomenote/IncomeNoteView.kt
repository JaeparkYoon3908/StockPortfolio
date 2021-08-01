package com.yjpapp.stockportfolio.function.incomenote

import com.yjpapp.stockportfolio.localdb.sqlte.data.IncomeNoteInfo
import com.yjpapp.stockportfolio.model.IncomeNoteModel

/**
 * IncomeNoteFragment의 View
 *
 * @author Yoon Jae-park
 * @since 2020.12
 */
interface IncomeNoteView {
    fun bindTotalGainData()

    fun changeFilterText(text: String)

    fun showAddButton()

    fun showFilterDialog()

    fun showInputDialog(editMode: Boolean, incomeNoteInfo: IncomeNoteModel.IncomeNoteList?)

    fun hideAddButton()

    fun scrollTopPosition(topPosition: Int)

    fun setAdapter(incomeNoteListAdapter: IncomeNoteListAdapter?)

}