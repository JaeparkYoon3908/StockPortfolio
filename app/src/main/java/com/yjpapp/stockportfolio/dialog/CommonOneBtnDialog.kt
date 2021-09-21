package com.yjpapp.stockportfolio.dialog

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.databinding.CommonDialogOneBtnBinding

class CommonOneBtnDialog(
    val mContext: Context,
    val commonOneBtnData: CommonOneBtnData
) : AlertDialog(mContext) {
    data class CommonOneBtnData(
        var noticeText: String = "",
        var btnText: String = "",
        var btnListener: OnClickListener
    )

    private var _binding: CommonDialogOneBtnBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.common_dialog_one_btn,
            null,
            false
        )
        setContentView(binding.root)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        initData()
    }
    private fun initData() {
        binding.apply {
            data = commonOneBtnData
            btnConfirm.setOnClickListener {
                commonOneBtnData.btnListener.onClick(it, this@CommonOneBtnDialog)
            }
        }
    }

    interface OnClickListener {
        fun onClick(view: View, dialog: CommonOneBtnDialog)
    }
}