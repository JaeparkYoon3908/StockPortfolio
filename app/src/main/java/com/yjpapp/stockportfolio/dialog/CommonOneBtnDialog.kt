package com.yjpapp.stockportfolio.dialog

import android.app.AlertDialog
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.databinding.CommonDialogOneBtnBinding
import com.yjpapp.stockportfolio.util.Utils

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
        setWidthHeight()
        initData()

    }
    private fun setWidthHeight() {
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
        //사이즈 설정
        params?.width = (deviceWidth * 0.8).toInt()
        params?.height = Utils.dpToPx(200)
        window?.attributes = params as WindowManager.LayoutParams
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