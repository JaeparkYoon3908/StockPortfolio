package com.yjpapp.stockportfolio.dialog

import android.app.AlertDialog
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.databinding.CommonDialogOneBtnBinding
import com.yjpapp.stockportfolio.extension.setOnSingleClickListener

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
        dialogResize(mContext, this@CommonOneBtnDialog, 0.8f, 0.25f)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        initData()
    }

    private fun initData() {
        binding.apply {
            data = commonOneBtnData
            btnConfirm.setOnSingleClickListener {
                commonOneBtnData.btnListener.onClick(it, this@CommonOneBtnDialog)
            }
        }
    }

    interface OnClickListener {
        fun onClick(view: View, dialog: CommonOneBtnDialog)
    }

    private fun dialogResize(context: Context, dialog: AlertDialog, width: Float, height: Float){
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        if (Build.VERSION.SDK_INT < 30){
            val display = windowManager.defaultDisplay
            val size = Point()

            display.getSize(size)

            val window = dialog.window

            val x = (size.x * width).toInt()
            val y = (size.y * height).toInt()

            window?.setLayout(x, y)

        } else {
            val rect = windowManager.currentWindowMetrics.bounds

            val window = dialog.window
            val x = (rect.width() * width).toInt()
            val y = (rect.height() * height).toInt()

            window?.setLayout(x, y)
        }
    }
}