package com.yjpapp.stockportfolio.ui.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.databinding.DialogMyStockFilterBinding
import com.yjpapp.stockportfolio.ui.presenter.MyStockPresenter
import com.yjpapp.stockportfolio.util.Utils

class MyStockFilterDialog (myStockPresenter: MyStockPresenter): BottomSheetDialogFragment() {

//    private lateinit var rootView: View
    private var _viewBinding: DialogMyStockFilterBinding? = null
    private val viewBinding get() = _viewBinding!!
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
        _viewBinding = DialogMyStockFilterBinding.inflate(inflater, container, false)
//        rootView = inflater.inflate(R.layout.dialog_income_note_filter, container, false)
        return viewBinding.root
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

            R.id.txt_MyStockDialog_cancel -> {
                dismiss()
            }
            R.id.txt_MyStockDialog_all -> {
                myStockPresenter.onAllFilterClicked()
                dismiss()
            }
            R.id.txt_MyStockDialog_gain -> {
                myStockPresenter.onGainFilterClicked()
                dismiss()
            }
            R.id.txt_MyStockDialog_loss -> {
                myStockPresenter.onLossFilterClicked()
                dismiss()
            }
        }
    }

    private fun initLayout(){
        viewBinding.apply {
            txtMyStockDialogCancel.setOnClickListener(onClickListener)
            txtMyStockDialogAll.setOnClickListener(onClickListener)
            txtMyStockDialogGain.setOnClickListener(onClickListener)
            txtMyStockDialogLoss.setOnClickListener(onClickListener)
        }
    }

}