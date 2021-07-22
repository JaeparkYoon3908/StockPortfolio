package com.yjpapp.stockportfolio.function.incomenote

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.databinding.DialogIncomeNoteFilterBinding
import com.yjpapp.stockportfolio.util.Utils

class IncomeNoteFilterDialog(incomeNotePresenter: IncomeNotePresenter): BottomSheetDialogFragment() {

//    private lateinit var rootView: View
    private var _viewBinding: DialogIncomeNoteFilterBinding? = null
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
//        rootView = inflater.inflate(R.layout.dialog_income_note_filter, container, false)
        _viewBinding = DialogIncomeNoteFilterBinding.inflate(inflater, container, false)

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

            R.id.txt_IncomeNoteFilterDialog_cancel -> {
                dismiss()
            }
            R.id.txt_IncomeNoteFilterDialog_all -> {
                incomeNotePresenter.onAllFilterClicked()
                dismiss()
            }
            R.id.txt_IncomeNoteFilterDialog_gain -> {
                incomeNotePresenter.onGainFilterClicked()
                dismiss()
            }
            R.id.txt_IncomeNoteFilterDialog_loss -> {
                incomeNotePresenter.onLossFilterClicked()
                dismiss()

            }
        }
    }

    private fun initLayout(){
        viewBinding.apply {
            txtIncomeNoteFilterDialogCancel.setOnClickListener(onClickListener)
            txtIncomeNoteFilterDialogAll.setOnClickListener(onClickListener)
            txtIncomeNoteFilterDialogGain.setOnClickListener(onClickListener)
            txtIncomeNoteFilterDialogLoss.setOnClickListener(onClickListener)
        }
    }

}