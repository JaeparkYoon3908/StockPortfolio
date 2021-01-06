package com.yjpapp.stockportfolio.ui.dialog

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.ui.presenter.IncomeNotePresenter
import kotlinx.android.synthetic.main.dialog_edit_income_note.*
import java.text.DecimalFormat
import java.util.*

/**
 *
 */

class IncomeNoteInputDialog(mContext: Context, incomeNotePresenter: IncomeNotePresenter, position: Int) : AlertDialog(mContext) {
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

    private val moneySymbol = Currency.getInstance(Locale.KOREA).symbol

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_edit_income_note)

        initLayout()
    }

    private fun initLayout(){

        et_purchase_date.setOnClickListener(onClickListener)
        et_sell_date.setOnClickListener(onClickListener)
        txt_cancel.setOnClickListener(onClickListener)
        EditMainDialog_MainContainer.setOnClickListener(onClickListener)

        et_purchase_price.addTextChangedListener(textWatcher)
        et_sell_price.addTextChangedListener(textWatcher)

        txt_purchase_price_symbol.text = moneySymbol
        txt_sell_price_symbol.text = moneySymbol

        window?.setBackgroundDrawableResource(R.color.color_80000000)
        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
    }

    private val onClickListener = View.OnClickListener { view: View? ->
        when (view?.id){
            R.id.txt_complete -> {
                incomeNotePresenter.onInputDialogCompleteClicked(this, false, position)
            }
            R.id.et_purchase_date -> {
                val calendar = Calendar.getInstance()
                val currentYear = calendar.get(Calendar.YEAR)
                val currentMonth = calendar.get(Calendar.MONTH)
                val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(mContext,
                    DatePickerDialog.OnDateSetListener { _: View, year, monthOfYear, dayOfMonth ->
                        //사용자가 캘린더에서 확인버튼을 눌렀을 때 콜백
                        purchaseYear = year.toString()
                        purchaseMonth = if (monthOfYear + 1 < 10) {
                            "0" + (monthOfYear + 1).toString()
                        } else {
                            (monthOfYear + 1).toString()
                        }
                        purchaseDay = if (dayOfMonth < 10) {
                            "0$dayOfMonth"
                        } else {
                            dayOfMonth.toString()
                        }
                        uiHandler.sendEmptyMessage(MSG.PURCHASE_DATE_DATA_INPUT)
                    },
                    currentYear,
                    currentMonth,
                    currentDay)
                datePickerDialog.show()
            }
            R.id.et_sell_date -> {
                val calendar = Calendar.getInstance()
                val currentYear = calendar.get(Calendar.YEAR)
                val currentMonth = calendar.get(Calendar.MONTH)
                val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
                val datePickerDialog = DatePickerDialog(mContext,
                    DatePickerDialog.OnDateSetListener { _: View, year, monthOfYear, dayOfMonth ->
                        //사용자가 캘린더에서 확인버튼을 눌렀을 때 콜백
                        sellYear = year.toString()
                        sellMonth = if (monthOfYear + 1 < 10) {
                            "0" + (monthOfYear + 1).toString()
                        } else {
                            (monthOfYear + 1).toString()
                        }
                        sellDay = if (dayOfMonth < 10) {
                            "0$dayOfMonth"
                        } else {
                            dayOfMonth.toString()
                        }
                        uiHandler.sendEmptyMessage(MSG.SELL_DATE_DATA_INPUT)
                    },
                    currentYear,
                    currentMonth,
                    currentDay)
                datePickerDialog.show()
            }
        }
    }

    //3자리마다 콤마 찍어주는 코드
    private val decimalFormat = DecimalFormat("###,###")
    private var result = "";
    private val textWatcher = object: TextWatcher{
        override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
            if(!TextUtils.isEmpty(charSequence.toString()) && charSequence.toString() != result){
                result = decimalFormat.format(charSequence.toString().replace(",", "").toDouble())
                if(et_purchase_price.hasFocus()){
                    et_purchase_price.setText(result)
                    et_purchase_price.setSelection(result.length) //커서를 오른쪽 끝으로 보낸다.
                }else if(et_sell_price.hasFocus()){
                    et_sell_price.setText(result)
                    et_sell_price.setSelection(result.length)
                }
            }
            if(charSequence?.length!! == 0 ){
                if(et_purchase_price.hasFocus()){
                    txt_purchase_price_symbol.setTextColor(getContext().getColor(R.color.color_666666))
                }else if(et_sell_price.hasFocus()){
                    txt_sell_price_symbol.setTextColor(getContext().getColor(R.color.color_666666))
                }
            }else{
                if(et_purchase_price.hasFocus()){
                    txt_purchase_price_symbol.setTextColor(getContext().getColor(R.color.color_222222))
                }else if(et_sell_price.hasFocus()){
                    txt_sell_price_symbol.setTextColor(getContext().getColor(R.color.color_222222))
                }
            }
        }

        override fun beforeTextChanged(
            charSequence: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) {

        }

        override fun afterTextChanged(editable: Editable?) {

        }
    }
    private val uiHandler = Handler(Looper.getMainLooper(), UIHandler())
    private inner class UIHandler: Handler.Callback {
        override fun handleMessage(msg: Message): Boolean {
            when (msg.what){
                MSG.PURCHASE_DATE_DATA_INPUT -> {
                    et_purchase_date.setText("$purchaseYear.$purchaseMonth.$purchaseDay")
                }
                MSG.SELL_DATE_DATA_INPUT -> {
                    et_sell_date.setText("$sellYear.$sellMonth.$sellDay")
                }
            }
            return true
        }
    }
}