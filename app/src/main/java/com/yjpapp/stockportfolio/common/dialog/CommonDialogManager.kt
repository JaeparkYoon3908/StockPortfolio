package com.yjpapp.stockportfolio.common.dialog

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager
import com.yjpapp.stockportfolio.R
import javax.inject.Singleton

object CommonDialogManager {
    private var isCommonOneBtnDialogShowing = false
    fun showCommonOneBtnDialog(
        context: Context,
        manager: FragmentManager,
        tag: String,
        msg: String
    ) {
        val dialogFragment = CommonOneBtnDialog(
            context,
            CommonOneBtnDialog.CommonOneBtnData(
                noticeText = msg,
                btnText = context.getString(R.string.Common_Ok),
                btnListener = { _: View, dialog: CommonOneBtnDialog ->
                    dialog.dismiss()
                }
            )
        )
        dialogFragment.dialog?.setOnDismissListener {
            isCommonOneBtnDialogShowing = false
        }

        if (!isCommonOneBtnDialogShowing) {
            dialogFragment.show(manager, tag)
            isCommonOneBtnDialogShowing = true
        }
    }
}