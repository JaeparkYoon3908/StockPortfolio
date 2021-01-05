package com.yjpapp.stockportfolio.ui.my_stock

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.util.Utils
import kotlinx.android.synthetic.main.dailog_income_note_filter.*

class MyStockFilterDialog (callback: MyStockFilterDialog.FilterClicked): BottomSheetDialogFragment() {

    interface FilterClicked{
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

    override fun onCreate(savedInstanceState: Bundle?) {
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        rootView = inflater.inflate(R.layout.dailog_income_note_filter, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        initLayout()
        super.onActivityCreated(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        val bottomSheet: View? = dialog?.findViewById(R.id.design_bottom_sheet)
        BottomSheetBehavior.from(bottomSheet!!).peekHeight = Utils.dpToPx(190)
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