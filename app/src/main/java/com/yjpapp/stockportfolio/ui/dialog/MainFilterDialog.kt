package com.yjpapp.stockportfolio.ui.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yjpapp.stockportfolio.R
import kotlinx.android.synthetic.main.dailog_main_filter.*

class MainFilterDialog(callback: MainFilterDialog.MainFilterClicked): BottomSheetDialogFragment() {
    interface MainFilterClicked{
        fun allSelect()
        fun gainSelect()
        fun lossSelect()
    }

    private lateinit var rootView: View
    private lateinit var mContext: Context

    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        rootView = inflater.inflate(R.layout.dailog_main_filter, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        initLayout()
        super.onActivityCreated(savedInstanceState)
    }

    private val onClickListener = View.OnClickListener { view: View? ->
        when(view?.id){

            R.id.txt_MainFilterDialog_cancel -> {
                dismiss()
            }
            R.id.txt_MainFilterDialog_all -> {
                callback.allSelect()
                dismiss()
            }
            R.id.txt_MainFilterDialog_gain -> {
                callback.gainSelect()
                dismiss()
            }
            R.id.txt_MainFilterDialog_loss -> {
                callback.lossSelect()
                dismiss()
            }
        }
    }

    private fun initLayout(){
        rootView.apply {
            txt_MainFilterDialog_cancel.setOnClickListener(onClickListener)
            txt_MainFilterDialog_all.setOnClickListener(onClickListener)
            txt_MainFilterDialog_gain.setOnClickListener(onClickListener)
            txt_MainFilterDialog_loss.setOnClickListener(onClickListener)
        }
    }

}