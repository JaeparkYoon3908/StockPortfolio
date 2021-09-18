package com.yjpapp.stockportfolio.widget

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.databinding.CustomDialogMonthYearPickerBinding
import com.yjpapp.stockportfolio.util.Utils

class CustomDatePickerDialog(var year: String, var month: String, var day: String = "") : DialogFragment() {
    private val TAG = CustomDatePickerDialog::class.java.simpleName
    private var selectedYear = 0
    private var selectedMonth = 0
    private var currentMaxDay = 0

    companion object {
        private const val MIN_YEAR = 2010
    }

    private lateinit var binding: CustomDialogMonthYearPickerBinding

    private var listener: DatePickerDialog.OnDateSetListener? = null

    fun setListener(listener: DatePickerDialog.OnDateSetListener?) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = CustomDialogMonthYearPickerBinding.inflate(requireActivity().layoutInflater)
//        val date = Date()
//        val cal: Calendar = Calendar.getInstance().apply { time = date }
        val nowYYYYMMDD: List<String> = Utils.getTodayYYMMDD()

        binding.pickerYear.apply {
//            val currentYear = cal.get(Calendar.YEAR)
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
            val displayValues = getDisplayedMonthValues()
            displayedValues = displayValues
            minValue = 1
            maxValue = displayValues.size
            value = if(day.isEmpty() || day.isEmpty()){
                nowYYYYMMDD[2].toInt()
            }else{
                day.toInt()
            }
        }

        return AlertDialog.Builder(requireContext())
                .setTitle("매도한 날짜를 선택해주세요.")
                .setView(binding.root)
                .setPositiveButton(getString(R.string.Common_Ok)) { _, _ ->
                    listener?.onDateSet(null,
                        binding.pickerYear.value,
                        binding.pickerMonth.value,
                        binding.pickerDay.value)
                }
                .setNegativeButton(getString(R.string.Common_Cancel)) { _, _ -> dialog?.cancel() }
                .create()
    }

    private fun getDisplayedMonthValues(): Array<String> {
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
}