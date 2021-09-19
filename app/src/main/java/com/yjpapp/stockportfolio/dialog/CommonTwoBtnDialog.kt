package com.yjpapp.stockportfolio.dialog

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.databinding.CommonDialogTwoBtnBinding

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
        initData()
    }
    private fun initData() {
        binding.apply {
            data = commonTwoBtnData
            //TODO data binding으로 바꿀 수 있는 방법은 없을까?
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