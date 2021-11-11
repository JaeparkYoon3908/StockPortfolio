package com.yjpapp.stockportfolio.dialog

import android.app.AlertDialog
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowManager
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

        setWidth()
        initData()
    }

    private fun setWidth() {
        val windowManager = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            mContext.display
        } else {
            windowManager.defaultDisplay
        }
        val size = Point()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            windowManager.currentWindowMetrics
        } else {
            display?.getSize(size)
        }
        val params: ViewGroup.LayoutParams? = window?.attributes
        val deviceWidth = size.x
        params?.width = (deviceWidth * 0.8).toInt()
        params?.height = WRAP_CONTENT
        window?.attributes = params as WindowManager.LayoutParams
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