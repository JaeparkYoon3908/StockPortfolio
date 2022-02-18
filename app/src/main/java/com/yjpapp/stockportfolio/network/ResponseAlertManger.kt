package com.yjpapp.stockportfolio.network

import android.content.Context
import android.view.View
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.common.dialog.CommonOneBtnDialog

object ResponseAlertManger {
    private var isShowDialog = false

    fun showErrorAlert(context: Context, msg: String) {
        if (!isShowDialog) {
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
            ).apply {
                setOnDismissListener {
                    isShowDialog = false
                }
                setOnShowListener {
                    isShowDialog = true
                }
            }.show()
        }

    }

    fun showNetworkConnectErrorAlert(context: Context) {
        if (!isShowDialog) {
            CommonOneBtnDialog(
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
            ).apply {
                setOnDismissListener {
                    isShowDialog = false
                }
                setOnShowListener {
                    isShowDialog = true
                }
            }.show()
        }
    }
}