package com.yjpapp.stockportfolio.common.dialog

import androidx.appcompat.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.databinding.CommonDialogTwoBtnBinding
import com.yjpapp.stockportfolio.util.StockUtils

class CommonTwoBtnDialog(
    private val mContext: Context,
    private val commonTwoBtnData: CommonTwoBtnData
) : AlertDialog(mContext) {
    data class CommonTwoBtnData(
        var noticeText: String = "",
        var leftBtnText: String = "",
        var rightBtnText: String = "",
        var leftBtnListener: (view: View, dialog: CommonTwoBtnDialog) -> Unit,
        var rightBtnListener: (view: View, dialog: CommonTwoBtnDialog) -> Unit,
    )

    private var _binding: CommonDialogTwoBtnBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.common_dialog_two_btn,
            null,
            false
        )
        setContentView(binding.root)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        StockUtils.setDialogWidthResize(mContext, this, 0.8f)

        initData()
    }

    private fun initData() {
        binding.apply {
            data = commonTwoBtnData
            btnLeft.setOnClickListener {
                commonTwoBtnData.leftBtnListener(it, this@CommonTwoBtnDialog)
            }
            btnRight.setOnClickListener {
                commonTwoBtnData.rightBtnListener(it, this@CommonTwoBtnDialog)
            }
        }
    }
}