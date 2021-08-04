package com.yjpapp.stockportfolio.function.incomenote

import android.widget.Toast
import com.yjpapp.stockportfolio.model.IncomeNoteModel
import es.dmoral.toasty.Toasty

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

    fun showToast(toast: Toast)

}