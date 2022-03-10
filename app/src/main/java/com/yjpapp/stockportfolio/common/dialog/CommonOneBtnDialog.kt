package com.yjpapp.stockportfolio.common.dialog

import androidx.appcompat.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.databinding.CommonDialogOneBtnBinding
import com.yjpapp.stockportfolio.extension.setOnSingleClickListener
import com.yjpapp.stockportfolio.util.DisplayUtils
import com.yjpapp.stockportfolio.util.StockUtils

class CommonOneBtnDialog(
    val mContext: Context,
    private val commonOneBtnData: CommonOneBtnData
) : AlertDialog(mContext) {
    data class CommonOneBtnData(
        var noticeText: String = "",
        var btnText: String = "",
        var btnListener: (view: View, dialog: CommonOneBtnDialog) -> Unit
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
        DisplayUtils.setDialogWidthResize(mContext, this, 0.8f)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        initData()
    }

    private fun initData() {
        binding.apply {
            data = commonOneBtnData
            btnConfirm.setOnSingleClickListener {
                commonOneBtnData.btnListener(it, this@CommonOneBtnDialog)
            }
        }
    }
}