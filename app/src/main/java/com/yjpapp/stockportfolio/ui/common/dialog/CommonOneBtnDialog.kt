package com.yjpapp.stockportfolio.ui.common.dialog

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.yjpapp.stockportfolio.databinding.CommonDialogOneBtnBinding
import com.yjpapp.stockportfolio.extension.setOnSingleClickListener
import com.yjpapp.stockportfolio.util.DisplayUtils

class CommonOneBtnDialog(
    val mContext: Context,
    private val commonOneBtnData: CommonOneBtnData
) : DialogFragment() {
    data class CommonOneBtnData(
        var noticeText: String = "",
        var btnText: String = "",
        var btnListener: (view: View, dialog: CommonOneBtnDialog) -> Unit
    )

    private var _binding: CommonDialogOneBtnBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CommonDialogOneBtnBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        DisplayUtils.setDialogWidthResize(mContext, dialog, 0.8f)
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