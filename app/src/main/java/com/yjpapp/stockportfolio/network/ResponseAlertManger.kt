package com.yjpapp.stockportfolio.network

import android.content.Context
import android.view.View
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.dialog.CommonOneBtnDialog

object ResponseAlertManger {

    fun showErrorAlert(context: Context, msg: String) {
        val dialog = CommonOneBtnDialog(
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
        )
        if (!dialog.isShowing) {
            dialog.show()
        }
    }

    fun showNetworkConnectErrorAlert(context: Context) {
        val dialog = CommonOneBtnDialog(
            context,
            CommonOneBtnDialog.CommonOneBtnData(
                noticeText = context.getString(R.string.Error_Msg_Network_Connect_Exception),
                btnText = context.getString(R.string.Common_Ok),
                btnListener = object : CommonOneBtnDialog.OnClickListener {
                    override fun onClick(view: View, dialog: CommonOneBtnDialog) {
                        dialog.dismiss()
                    }
                }
            )
        )
        if (!dialog.isShowing) {
            dialog.show()
        }
    }
}