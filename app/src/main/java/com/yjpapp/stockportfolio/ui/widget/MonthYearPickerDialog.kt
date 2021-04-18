package com.yjpapp.stockportfolio.ui.widget

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.util.Util
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.databinding.DialogMonthYearPickerBinding
import com.yjpapp.stockportfolio.util.Utils
import java.text.SimpleDateFormat
import java.util.*

class MonthYearPickerDialog(var year: String, var month: String) : DialogFragment() {

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
        val nowYYMM: List<String> = Utils.getTodayYYMM()

        binding.pickerMonth.run {
            minValue = 1
            maxValue = 12
            value = if(month.isEmpty() || month.isEmpty()){
                nowYYMM[1].toInt()
            }else{
                month.toInt()
            }
            displayedValues = arrayOf("01", "02", "03", "04", "05", "06", "07",
                    "08", "09", "10", "11", "12")
        }

        binding.pickerYear.run {
//            val currentYear = cal.get(Calendar.YEAR)
            minValue = MIN_YEAR
            maxValue = nowYYMM[0].toInt()
            value = if(year.isEmpty() || year.isEmpty()){
                nowYYMM[0].toInt()
            }else{
                year.toInt()
            }
        }

        return AlertDialog.Builder(requireContext())
                .setTitle("매도한 날짜를 선택해주세요.")
                .setView(binding.root)
                .setPositiveButton(getString(R.string.Common_Ok)) { _, _ -> listener?.onDateSet(null, binding.pickerYear.value, binding.pickerMonth.value, 1) }
                .setNegativeButton(getString(R.string.Common_Cancel)) { _, _ -> dialog?.cancel() }
                .create()
    }
}