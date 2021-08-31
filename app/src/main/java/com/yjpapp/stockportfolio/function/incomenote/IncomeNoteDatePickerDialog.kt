package com.yjpapp.stockportfolio.function.incomenote

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.databinding.DialogIncomeNoteDatePickerBinding
import com.yjpapp.stockportfolio.util.Utils
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IncomeNoteDatePickerDialog(val incomeNotePresenter: IncomeNotePresenter) :
    BottomSheetDialogFragment() {
    private val TAG = IncomeNoteDatePickerDialog::class.java.simpleName
    private lateinit var mContext: Context
    private var _viewBinding: DialogIncomeNoteDatePickerBinding? = null
    private val viewBinding get() = _viewBinding!!
    private val MIN_YEAR = 2010
    var initStartYear = Utils.getTodayYYMM()[0]
    var initStartMonth = "01"
    var initEndYear = Utils.getTodayYYMM()[0]
    var initEndMonth = "12"

    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _viewBinding = DialogIncomeNoteDatePickerBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayout()
    }

    private val onClickListener = View.OnClickListener { view ->
        viewBinding.run {
            when (view.id) {
                btnConfirm.id -> {
                    if (startPickerYear.value > endPickerYear.value) {
                        Toasty.info(mContext, "시작연도가 종료연도 보다 큽니다.").show()
                        return@OnClickListener
                    } else if (startPickerYear.value == endPickerYear.value && startPickerMonth.value > endPickerMonth.value) {
                        Toasty.info(mContext, "시작월이 종료월보다 큽니다.").show()
                        return@OnClickListener
                    }
                    val startYYYY = startPickerYear.value.toString()
                    val startMM =
                        if (startPickerMonth.value < 10) "0" + startPickerMonth.value.toString()
                        else startPickerMonth.value.toString()

                    val endYYYY = endPickerYear.value.toString()
                    val endMM =
                        if (endPickerMonth.value < 10) "0" + endPickerMonth.value.toString()
                        else endPickerMonth.value.toString()
                    val startYYYYMM = "$startYYYY-$startMM-01"
                    val endYYYYMM = "$endYYYY-$endMM-01"
                    CoroutineScope(Dispatchers.Main).launch {
                        incomeNotePresenter.requestIncomeNoteList(mContext, startYYYYMM, endYYYYMM)
                    }

                    dismiss()
                }

                btnCancel.id -> {
                    dismiss()
                }
            }
        }
    }

    private fun initLayout() {
        val nowYYMM: List<String> = Utils.getTodayYYMM()
        viewBinding.apply {
            btnConfirm.setOnClickListener(onClickListener)
            btnCancel.setOnClickListener(onClickListener)
        }
        //시작 월
        viewBinding.startPickerMonth.run {
            minValue = 1
            maxValue = 12
            value = if (initStartMonth.isEmpty() || initStartMonth.isEmpty()) {
                nowYYMM[1].toInt()
            } else {
                initStartMonth.toInt()
            }
            displayedValues = arrayOf(
                "01", "02", "03", "04", "05", "06", "07",
                "08", "09", "10", "11", "12"
            )
        }
        //시작 연도
        viewBinding.startPickerYear.run {
            minValue = MIN_YEAR
            maxValue = nowYYMM[0].toInt()
            value = if (initStartYear.isEmpty() || initStartYear.isEmpty()) {
                nowYYMM[0].toInt()
            } else {
                initStartYear.toInt()
            }
        }
        //종료 월
        viewBinding.endPickerMonth.run {
            minValue = 1
            maxValue = 12
            value = if (initEndMonth.isEmpty() || initEndMonth.isEmpty()) {
                nowYYMM[1].toInt()
            } else {
                initEndMonth.toInt()
            }
            displayedValues = arrayOf(
                "01", "02", "03", "04", "05", "06", "07",
                "08", "09", "10", "11", "12"
            )
        }
        //종료 연도
        viewBinding.endPickerYear.run {
            minValue = MIN_YEAR
            maxValue = nowYYMM[0].toInt()
            value = if (initEndYear.isEmpty() || initEndYear.isEmpty()) {
                nowYYMM[0].toInt()
            } else {
                initEndYear.toInt()
            }
        }
    }
}