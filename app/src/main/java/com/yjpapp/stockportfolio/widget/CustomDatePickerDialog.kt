package com.yjpapp.stockportfolio.widget

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.databinding.DialogMonthYearPickerBinding
import com.yjpapp.stockportfolio.util.Utils
import java.lang.IndexOutOfBoundsException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class CustomDatePickerDialog(var year: String, var month: String, var day: String = "") : DialogFragment() {
    private val TAG = CustomDatePickerDialog::class.java.simpleName
    private var selectedYear = 0
    private var selectedMonth = 0
    private var currentMaxDay = 0

    companion object {
        private const val MIN_YEAR = 2010
    }

    private lateinit var binding: DialogMonthYearPickerBinding

    private var listener: DatePickerDialog.OnDateSetListener? = null

    fun setListener(listener: DatePickerDialog.OnDateSetListener?) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogMonthYearPickerBinding.inflate(requireActivity().layoutInflater)
//        val date = Date()
//        val cal: Calendar = Calendar.getInstance().apply { time = date }
        val nowYYMM: List<String> = Utils.getTodayYYMMDD()

        binding.pickerYear.apply {
//            val currentYear = cal.get(Calendar.YEAR)
            minValue = MIN_YEAR
            maxValue = nowYYMM[0].toInt()
            value = if(year.isEmpty() || year.isEmpty()){
                nowYYMM[0].toInt()
            }else{
                year.toInt()
            }
            selectedYear = value
        }

        binding.pickerMonth.apply {
            minValue = 1
            maxValue = 12
            value = if(month.isEmpty() || month.isEmpty()){
                nowYYMM[1].toInt()
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
            minValue = 0
            maxValue = displayValues.size - 1
            value = if(day.isEmpty() || day.isEmpty()){
                nowYYMM[2].toInt()
            }else{
                day.toInt()
            }
        }
        initListener()

        return AlertDialog.Builder(requireContext())
                .setTitle("매도한 날짜를 선택해주세요.")
                .setView(binding.root)
                .setPositiveButton(getString(R.string.Common_Ok)) { _, _ ->
                    listener?.onDateSet(null,
                        binding.pickerYear.value,
                        binding.pickerMonth.value,
                        binding.pickerDay.value + 1)
                }
                .setNegativeButton(getString(R.string.Common_Cancel)) { _, _ -> dialog?.cancel() }
                .create()
    }

    private fun initListener() {
        binding.pickerYear.setOnValueChangedListener { picker, oldVal, newVal ->
            selectedYear = newVal
            binding.pickerDay.apply {
                if (value == maxValue) {
                    //TODO maxValue 값이 줄어듬으로써 outOfIndex 수정하기
                    Log.d(TAG, "이프문 진입")
                }
                val displayValues = getDisplayedMonthValues()
                displayedValues = displayValues
                maxValue = displayValues.size - 1
            }
        }
        binding.pickerMonth.setOnValueChangedListener { picker, oldVal, newVal ->
            selectedMonth = newVal
            binding.pickerDay.apply {
                val displayValues = getDisplayedMonthValues()
                if (value == maxValue) {
                    //TODO maxValue 값이 줄어듬으로써 outOfIndex 수정하기
                    Log.d(TAG, "이프문 진입")
                }
                maxValue = displayValues.size - 1
                displayedValues = displayValues
            }
        }
    }

    private fun getDisplayedMonthValues(): Array<String> {
        val result = mutableListOf<String>()
        val month = if (selectedMonth < 10) {
            "0$selectedMonth"
        } else {
            "$selectedMonth"
        }
        val date = "$selectedYear/$month/01"
        val dateFormat = SimpleDateFormat("yyyy/MM/dd")
        val convertedDate: Date = dateFormat.parse(date)
        val c = Calendar.getInstance()
        c.time = convertedDate
        val lastMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (i in 1..lastMonth) {
            if (i < 10) {
                result.add("0$i")
            } else {
                result.add("$i")
            }
        }
        return result.toTypedArray()
    }
}