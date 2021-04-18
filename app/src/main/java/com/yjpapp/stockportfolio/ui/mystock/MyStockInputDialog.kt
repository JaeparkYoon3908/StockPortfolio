package com.yjpapp.stockportfolio.ui.mystock

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.DatePicker
import androidx.databinding.DataBindingUtil
import com.ibotta.android.support.pickerdialogs.SupportedDatePickerDialog
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.databinding.DialogInputMyStockBinding
import com.yjpapp.stockportfolio.ui.incomenote.IncomeNoteInputDialog


class MyStockInputDialog(context: Context, private val myStockViewModel: MyStockViewModel):
        AlertDialog(context), SupportedDatePickerDialog.OnDateSetListener {
    companion object{
        @Volatile private var instance: MyStockInputDialog? = null
        @JvmStatic
        fun getInstance(context: Context, myStockViewModel: MyStockViewModel): MyStockInputDialog =
                instance ?: synchronized(this) {
                    instance ?: MyStockInputDialog(context, myStockViewModel).also {
                        instance = it
                    }
                }

    }
    object MSG{
        const val SELL_DATE_DATA_INPUT: Int = 0
    }
    val binding: DialogInputMyStockBinding = DataBindingUtil.inflate(
        LayoutInflater.from(context),
        R.layout.dialog_input_my_stock,
        null,
        false
    )
    var purchaseYear = ""
    var purchaseMonth = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.viewModel = myStockViewModel
        window?.setBackgroundDrawableResource(R.color.color_80000000)
        //EditText focus 했을 때 키보드가 보이도록 설정
        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        initLayout()
    }

    private fun initLayout(){

    }

    val uiHandler = Handler(Looper.getMainLooper(), UIHandler())
    private inner class UIHandler: Handler.Callback {
        override fun handleMessage(msg: Message): Boolean {
            when (msg.what){
                MSG.SELL_DATE_DATA_INPUT -> {
                    binding.etSellDate.setText("$purchaseYear.$purchaseMonth")
                }
            }
            return true
        }
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        purchaseYear = year.toString()
        purchaseMonth = if (month + 1 < 10) {
            "0" + (month + 1).toString()
        } else {
            (month + 1).toString()
        }
        uiHandler.sendEmptyMessage(IncomeNoteInputDialog.MSG.PURCHASE_DATE_DATA_INPUT)
    }
}