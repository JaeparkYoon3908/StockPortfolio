package com.yjpapp.stockportfolio.network

import android.content.Context
import android.view.View
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.dialog.CommonOneBtnDialog

object ResponseAlertManger {

    fun showErrorAlert(context: Context, msg: String) {
        CommonOneBtnDialog(
            context,
            CommonOneBtnDialog.CommonOneBtnData(
                noticeText = msg,
                btnText = context.getString(R.string.Common_Ok),
                btnListener = object : CommonOneBtnDialog.OnClickListener {
                    override fun onClick(view: View, dialog: CommonOneBtnDialog) {
                        dialog.dismiss()
                    }
                }
            )
        ).show()
    }
}