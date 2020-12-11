package com.yjpapp.stockportfolio.ui.dialog

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.os.Handler
import android.os.Message
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import com.yjpapp.stockportfolio.R
import kotlinx.android.synthetic.main.dialog_add_portfolio.*
import java.text.DecimalFormat
import java.util.*

class CustomDialog(context: Context): BaseAlertDialog(context, R.layout.dialog_add_portfolio) {

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

    private val builder: AlertDialog.Builder by lazy {
        AlertDialog.Builder(context)
    }

//    private val view: View by lazy {
//        View.inflate(context, R.layout.dialog_add_portfolio, null)
//    }

    private var dialog: AlertDialog? = null

//    init {
//        apply {
//            et_purchase_date.setOnClickListener(onClickListener)
//            et_sell_date.setOnClickListener(onClickListener)
//            txt_cancel.setOnClickListener(onClickListener)
//            EditMainDialog_MainContainer.setOnClickListener(onClickListener)
//
//            et_purchase_price.addTextChangedListener(textWatcher)
//            et_sell_price.addTextChangedListener(textWatcher)
//
//            txt_purchase_price_symbol.text = moneySymbol
//            txt_sell_price_symbol.text = moneySymbol
//        }
//    }

//
//    fun setTitle(@StringRes titleId: Int): CustomDialog {
//        view.titleTextView.text = context.getText(titleId)
//        return this
//    }
//
//    fun setTitle(title: CharSequence): CustomDialog {
//        view.titleTextView.text = title
//        return this
//    }
//
//    fun setMessage(@StringRes messageId: Int): CustomDialog {
//        view.messageTextView.text = context.getText(messageId)
//        return this
//    }
//
//    fun setMessage(message: CharSequence): CustomDialog {
//        view.messageTextView.text = message
//        return this
//    }
//
//    fun setPositiveButton(@StringRes textId: Int, listener: (view: View) -> (Unit)): CustomDialog {
//        view.positiveButton.apply {
//            text = context.getText(textId)
//            setOnClickListener(listener)
//        }
//        return this
//    }
//
//    fun setPositiveButton(text: CharSequence, listener: (view: View) -> (Unit)): CustomDialog {
//        view.positiveButton.apply {
//            this.text = text
//            setOnClickListener(listener)
//        }
//        return this
//    }
//
//    fun setNegativeButton(@StringRes textId: Int, listener: (view: View) -> (Unit)): CustomDialog {
//        view.negativeButton.apply {
//            text = context.getText(textId)
//            this.text = text
//            setOnClickListener(listener)
//        }
//        return this
//    }
//
//    fun setNegativeButton(text: CharSequence, listener: (view: View) -> (Unit)): CustomDialog {
//        view.negativeButton.apply {
//            this.text = text
//            setOnClickListener(listener)
//        }
//        return this
//    }

//    fun create() {
//        dialog = builder.create()
//    }
//
//    fun show() {
//        super.show()
//        dialog = builder.create()
//        dialog?.show()
//        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, Utils.dpToPx(350))
//    }
//
//    fun dismiss() {
//        dialog?.dismiss()
//    }

    private val onClickListener = View.OnClickListener { view: View? ->
        when (view?.id){
            R.id.et_purchase_date -> {
                val calendar = Calendar.getInstance()
                val currentYear = calendar.get(Calendar.YEAR)
                val currentMonth = calendar.get(Calendar.MONTH)
                val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { _:View, year, monthOfYear, dayOfMonth ->
                    //사용자가 캘린더에서 확인버튼을 눌렀을 때 콜백
                    purchaseYear = year.toString()
                    purchaseMonth = if(monthOfYear+1 < 10){
                        "0" + (monthOfYear+1).toString()
                    }else{
                        (monthOfYear+1).toString()
                    }
                    purchaseDay = if(dayOfMonth < 10){
                        "0$dayOfMonth"
                    }else{
                        dayOfMonth.toString()
                    }
                    uiHandler.sendEmptyMessage(EditPortfolioDialog.MSG.PURCHASE_DATE_DATA_INPUT)
                }, currentYear, currentMonth, currentDay)
                datePickerDialog.show()
            }
            R.id.et_sell_date -> {
                val calendar = Calendar.getInstance()
                val currentYear = calendar.get(Calendar.YEAR)
                val currentMonth = calendar.get(Calendar.MONTH)
                val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
                val datePickerDialog = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { _:View, year, monthOfYear, dayOfMonth ->
                    //사용자가 캘린더에서 확인버튼을 눌렀을 때 콜백
                    sellYear = year.toString()
                    sellMonth = if(monthOfYear+1 < 10){
                        "0" + (monthOfYear+1).toString()
                    }else{
                        (monthOfYear+1).toString()
                    }
                    sellDay = if(dayOfMonth < 10){
                        "0$dayOfMonth"
                    }else{
                        dayOfMonth.toString()
                    }
                    uiHandler.sendEmptyMessage(EditPortfolioDialog.MSG.SELL_DATE_DATA_INPUT)
                }, currentYear, currentMonth, currentDay)
                datePickerDialog.show()
            }
        }
    }

    //3자리마다 콤마 찍어주는 코드
    private val decimalFormat = DecimalFormat("###,###")
    private var result = "";
    private val textWatcher = object: TextWatcher {
        override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
            if(!TextUtils.isEmpty(charSequence.toString()) && charSequence.toString() != result){
                result = decimalFormat.format(charSequence.toString().replace(",","").toDouble())
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

        override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun afterTextChanged(editable: Editable?) {

        }
    }
    private val uiHandler = Handler(UIHandler())
    private inner class UIHandler: Handler.Callback {
        override fun handleMessage(msg: Message): Boolean {
            when (msg.what){

                EditPortfolioDialog.MSG.PURCHASE_DATE_DATA_INPUT -> {
                    et_purchase_date.setText("$purchaseYear.$purchaseMonth.$purchaseDay")
                }
                EditPortfolioDialog.MSG.SELL_DATE_DATA_INPUT -> {
                    et_sell_date.setText("$sellYear.$sellMonth.$sellDay")
                }
            }
            return true
        }
    }
}