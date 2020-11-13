package com.yjpapp.stockportfolio.ui.dialog

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import com.yjpapp.stockportfolio.R
import kotlinx.android.synthetic.main.dialog_add_portfolio.*
import java.util.*

class AddPortfolioDialog(context: Context) : Dialog(context, android.R.style.Theme_Translucent_NoTitleBar) {
    object MSG{
        const val PURCHASE_DATE_DATA_INPUT: Int = 0
        const val SELL_DATE_DATA_INPUT: Int = 1
    }
    private var purchaseYear: String? = null
    private var purchaseMonth: String? = null
    private var purchaseDay: String? = null

    private var sellYear: String? = null
    private var sellMonth: String? = null
    private var sellDay: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_add_portfolio)
        initLayout()
    }

    private fun initLayout(){
        et_purchase_date.setOnClickListener(onClickListener)
        et_sell_date.setOnClickListener(onClickListener)
        txt_cancel.setOnClickListener(onClickListener)
    }

    private val onClickListener = View.OnClickListener { view: View? ->
        when (view?.id){
            R.id.et_purchase_date -> {
                val calendar = Calendar.getInstance()
                val currentYear = calendar.get(Calendar.YEAR)
                val currentMonth = calendar.get(Calendar.MONTH)
                val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    //사용자가 캘린더에서 확인버튼을 눌렀을 때 콜백
                    purchaseYear = year.toString()
                    purchaseMonth = (monthOfYear+1).toString()
                    purchaseDay = dayOfMonth.toString()
                    uiHandler.sendEmptyMessage(MSG.PURCHASE_DATE_DATA_INPUT)
                }, currentYear, currentMonth, currentDay)
                datePickerDialog.show()
            }
            R.id.et_sell_date -> {
                val calendar = Calendar.getInstance()
                val currentYear = calendar.get(Calendar.YEAR)
                val currentMonth = calendar.get(Calendar.MONTH)
                val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
                val datePickerDialog = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    //사용자가 캘린더에서 확인버튼을 눌렀을 때 콜백
                    sellYear = year.toString()
                    sellMonth = (monthOfYear+1).toString()
                    sellDay = dayOfMonth.toString()
                    uiHandler.sendEmptyMessage(MSG.SELL_DATE_DATA_INPUT)
                }, currentYear, currentMonth, currentDay)
                datePickerDialog.show()
            }
            R.id.txt_cancel -> {
                dismiss()
            }
            R.id.txt_complete -> {
//                val subject
//                val purchaseDate = purchaseYear + purchaseMonth + purchaseDay
//                val sellDate = sellYear + sellMonth + sellDay

            }
        }
    }
    private val uiHandler = Handler(UIHandler())
    private inner class UIHandler: Handler.Callback {
        override fun handleMessage(msg: Message): Boolean {
            when (msg.what){
                MSG.PURCHASE_DATE_DATA_INPUT -> {
                    et_purchase_date.setText(purchaseYear + purchaseMonth + purchaseDay)
                }
                MSG.SELL_DATE_DATA_INPUT -> {
                    et_sell_date.setText(sellYear + sellMonth + sellDay)
                }
            }
            return true
        }
    }
}