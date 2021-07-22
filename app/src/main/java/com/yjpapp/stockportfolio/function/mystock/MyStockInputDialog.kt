package com.yjpapp.stockportfolio.function.mystock

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
import com.yjpapp.stockportfolio.function.incomenote.IncomeNoteInputDialog


class MyStockInputDialog(context: Context):
        AlertDialog(context), SupportedDatePickerDialog.OnDateSetListener, MyStockInputDialogController {
    companion object{
        @Volatile private var instance: MyStockInputDialog? = null
        @JvmStatic
        fun getInstance(context: Context): MyStockInputDialog =
                instance ?: synchronized(this) {
                    instance ?: MyStockInputDialog(context).also {
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
        window?.setBackgroundDrawableResource(R.color.color_80000000)
        //EditText focus 했을 때 키보드가 보이도록 설정
        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        binding.executePendingBindings()
    }

    val uiHandler = Handler(Looper.getMainLooper(), UIHandler())
    private inner class UIHandler: Handler.Callback {
        override fun handleMessage(msg: Message): Boolean {
            when (msg.what){
                MSG.SELL_DATE_DATA_INPUT -> {
                    binding.etPurchaseDate.setText("$purchaseYear.$purchaseMonth")
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

    override fun changeMoneySymbolTextColor(color: Int) {
        binding.txtPurchasePriceSymbol.setTextColor(context.getColor(color))
    }

    private fun onCancelButtonClick(){
        dismiss()
    }

}