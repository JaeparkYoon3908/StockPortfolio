package com.yjpapp.stockportfolio.dialog

import androidx.appcompat.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.databinding.CommonDialogTwoBtnBinding
import com.yjpapp.stockportfolio.util.Utils

class CommonTwoBtnDialog(
    private val mContext: Context,
    private val commonTwoBtnData: CommonTwoBtnData
) : AlertDialog(mContext) {
    data class CommonTwoBtnData(
        var noticeText: String = "",
        var leftBtnText: String = "",
        var rightBtnText: String = "",
        var leftBtnListener: OnClickListener,
        var rightBtnListener: OnClickListener
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
        Utils.setDialogWidth(mContext, this, 0.8)

        initData()
    }

    private fun initData() {
        binding.apply {
            data = commonTwoBtnData
            btnLeft.setOnClickListener {
                commonTwoBtnData.leftBtnListener.onClick(it, this@CommonTwoBtnDialog)
            }
            btnRight.setOnClickListener {
                commonTwoBtnData.rightBtnListener.onClick(it, this@CommonTwoBtnDialog)
            }
        }
    }

    interface OnClickListener {
        fun onClick(view: View, dialog: CommonTwoBtnDialog)
    }
}