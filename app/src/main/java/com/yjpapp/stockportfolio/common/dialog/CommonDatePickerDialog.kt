package com.yjpapp.stockportfolio.common.dialog

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.databinding.CustomDialogMonthYearPickerBinding
import com.yjpapp.stockportfolio.util.Utils

class CommonDatePickerDialog(
    val mContext: Context,
    var year: String,
    var month: String,
    var day: String = ""
) : AlertDialog(mContext) {
    private val TAG = CommonDatePickerDialog::class.java.simpleName
    private var _binding: CustomDialogMonthYearPickerBinding? = null
    private val binding get() = _binding!!
    private var selectedYear = 0
    private var selectedMonth = 0
    private var currentMaxDay = 0

    companion object {
        private const val MIN_YEAR = 2010
    }

    private var listener: DatePickerDialog.OnDateSetListener? = null

    fun setListener(listener: DatePickerDialog.OnDateSetListener?) {
        this.listener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.custom_dialog_month_year_picker,
            null,
            false
        )
        setContentView(binding.root)
        Utils.setDialogWidthResize(mContext, this, 0.85f)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        initView()
    }

    private fun initView() {
        val nowYYYYMMDD: List<String> = Utils.getTodayYYMMDD()

        binding.pickerYear.apply {
            minValue = MIN_YEAR
            maxValue = nowYYYYMMDD[0].toInt()
            value = if(year.isEmpty() || year.isEmpty()){
                nowYYYYMMDD[0].toInt()
            }else{
                year.toInt()
            }
            selectedYear = value
        }

        binding.pickerMonth.apply {
            minValue = 1
            maxValue = 12
            value = if(month.isEmpty() || month.isEmpty()){
                nowYYYYMMDD[1].toInt()
            }else{
                month.toInt()
            }

            displayedValues = arrayOf("01", "02", "03", "04", "05", "06", "07",
                "08", "09", "10", "11", "12")
            selectedMonth = value
        }

        binding.pickerDay.apply {
            val displayValues = getDisplayedDayValues()
            displayedValues = displayValues
            minValue = 1
            maxValue = displayValues.size
            value = if(day.isEmpty() || day.isEmpty()){
                nowYYYYMMDD[2].toInt()
            }else{
                day.toInt()
            }
        }

        binding.btnLeft.setOnClickListener {
            dismiss()
        }

        binding.btnRight.setOnClickListener {
            listener?.onDateSet(null,
                binding.pickerYear.value,
                binding.pickerMonth.value,
                binding.pickerDay.value
            )
            dismiss()
        }
    }

    private fun getDisplayedDayValues(): Array<String> {
        val result = mutableListOf<String>()
//        val month = if (selectedMonth < 10) {
//            "0$selectedMonth"
//        } else {
//            "$selectedMonth"
//        }
//        val date = "$selectedYear/$month/01"
//        val dateFormat = SimpleDateFormat("yyyy/MM/dd")
//        val convertedDate: Date = dateFormat.parse(date)
//        val c = Calendar.getInstance()
//        c.time = convertedDate
//        val lastMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (i in 1..31) {
            if (i < 10) {
                result.add("0$i")
            } else {
                result.add("$i")
            }
        }
        return result.toTypedArray()
    }

    interface OnClickListener {
        fun onClick(view: View, dialog: CommonTwoBtnDialog)
    }
}