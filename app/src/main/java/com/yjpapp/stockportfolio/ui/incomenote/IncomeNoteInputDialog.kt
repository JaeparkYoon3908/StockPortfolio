package com.yjpapp.stockportfolio.ui.incomenote

import android.app.AlertDialog
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
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.ibotta.android.support.pickerdialogs.SupportedDatePickerDialog
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.constance.AppConfig
import com.yjpapp.stockportfolio.database.sqlte.data.IncomeNoteInfo
import com.yjpapp.stockportfolio.util.Utils
import java.text.DecimalFormat
import java.util.*


class IncomeNoteInputDialog(mContext: Context, incomeNotePresenter: IncomeNotePresenter) : AlertDialog(mContext)
        , SupportedDatePickerDialog.OnDateSetListener{
    object MSG{
        const val PURCHASE_DATE_DATA_INPUT: Int = 0
        const val SELL_DATE_DATA_INPUT: Int = 1
    }
    var purchaseYear: String? = null
    var purchaseMonth: String? = null
//    private var purchaseDay: String? = null
    private var sellYear: String? = null
    private var sellMonth: String? = null
    private var sellDay: String? = null
    lateinit var etSellDate: EditText
//    lateinit var etSellDate: EditText
    lateinit var txtCancel: TextView
    lateinit var txtComplete: TextView
    lateinit var EditMainDialogMainContainer: ConstraintLayout
    lateinit var etPurchasePrice: EditText
    lateinit var etSellPrice: EditText
    lateinit var txtPurchasePriceSymbol: TextView
    lateinit var txtSellPriceSymbol: TextView
    lateinit var etSubjectName: EditText
    lateinit var etSellCount: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_input_income_note)
        initLayout()
    }

    private fun initLayout(){
        etSellDate = findViewById(R.id.et_sell_date)
//        etSellDate = findViewById(R.id.et_sell_date)
        txtCancel = findViewById(R.id.txt_cancel)
        txtComplete = findViewById(R.id.txt_complete)
        EditMainDialogMainContainer = findViewById(R.id.EditMainDialog_MainContainer)
        etPurchasePrice = findViewById(R.id.et_purchase_price)
        etSellPrice = findViewById(R.id.et_sell_price)
        txtPurchasePriceSymbol = findViewById(R.id.txt_purchase_price_symbol)
        txtSellPriceSymbol = findViewById(R.id.txt_sell_price_symbol)
        etSubjectName = findViewById(R.id.et_subject_name)
        etSellCount = findViewById(R.id.et_sell_count)

        etSellDate.setOnClickListener(onClickListener)
//        etSellDate.setOnClickListener(onClickListener)
        txtCancel.setOnClickListener(onClickListener)
        txtComplete.setOnClickListener(onClickListener)
        EditMainDialogMainContainer.setOnClickListener(onClickListener)

        etPurchasePrice.addTextChangedListener(textWatcher)
        etSellPrice.addTextChangedListener(textWatcher)

        txtPurchasePriceSymbol.text = AppConfig.moneySymbol
        txtSellPriceSymbol.text = AppConfig.moneySymbol

        window?.setBackgroundDrawableResource(R.color.color_80000000)
        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
    }

    private val onClickListener = View.OnClickListener { view: View? ->
        when (view?.id){
            txtCancel.id -> {
                dismiss()
            }
            txtComplete.id -> {
                //예외처리 (값을 모두 입력하지 않았을 때)
                if (etSubjectName.text.isEmpty() ||
//                    etPurchaseDate.text.isEmpty() ||
//                    etSellDate.text.isEmpty() ||
                        etPurchasePrice.text.isEmpty() ||
                        etSellPrice.text.isEmpty() ||
                        etSellCount.text.isEmpty()
                ) {
                    Toast.makeText(mContext, "필수 값을 모두 입력해야합니다.", Toast.LENGTH_LONG).show()
                    return@OnClickListener
                }

                //매매한 회사이름
                val subjectName = etSubjectName.text.toString()
                //매도일
                val sellDate = etSellDate.text.toString()
                //매수금액
                val purchasePrice = etPurchasePrice.text.toString()
                val purchasePriceNumber = Utils.getNumDeletedComma(purchasePrice)
                //매도금액
                val sellPrice = etSellPrice.text.toString()
                val sellPriceNumber = Utils.getNumDeletedComma(sellPrice)
                //매도수량
                val sellCount = etSellCount.text.toString().toInt()
                //수익
                val realPainLossesAmountNumber =
                        ((sellPriceNumber.toDouble() - purchasePriceNumber.toDouble()) * sellCount)
                val realPainLossesAmount = DecimalFormat("###,###").format(realPainLossesAmountNumber)
                //수익률
                val gainPercentNumber = Utils.calculateGainPercent(purchasePrice, sellPrice)
                val gainPercent = Utils.getRoundsPercentNumber(gainPercentNumber)

//                //날짜오류 예외처리
//                if (Utils.getNumDeletedDot(sellDate).toInt() > Utils.getNumDeletedDot(sellDate).toInt()) {
//                    Toast.makeText(mContext, "매도한 날짜가 매수한 날짜보다 앞서있습니다.", Toast.LENGTH_LONG).show()
//                    return@OnClickListener
//                }

                val dataInfo = IncomeNoteInfo(0, subjectName, realPainLossesAmount, sellDate,
                        gainPercent, purchasePrice, sellPrice, sellCount)
                incomeNotePresenter.onInputDialogCompleteClicked(dataInfo)
                dismiss()
            }
            etSellDate.id -> {

//                val calendar = Calendar.getInstance()
//                val currentYear = calendar.get(Calendar.YEAR)
//                val currentMonth = calendar.get(Calendar.MONTH)
//                val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
//                val date = Date()
//
//                MonthYearPickerDialog(date).apply {
//                    setListener { view, year, month, dayOfMonth ->
//                        Toast.makeText(requireContext(), "Set date: $year/$month/$dayOfMonth", Toast.LENGTH_LONG).show()
//                        findViewById<EditText>(R.id.et_sell_date).setText(year+month)
//                        etSellDate.setText(year+month)
//                    }
//                    show(childFragmentManager, "MonthYearPickerDialog")
//                }

//                val datePickerDialog = DatePickerDialog(mContext, R.style.MySpinnerDatePickerStyle, /*this,*/
//                        DatePickerDialog.OnDateSetListener { _: View, year, monthOfYear, dayOfMonth ->
//                            //사용자가 캘린더에서 확인버튼을 눌렀을 때 콜백
//                            purchaseYear = year.toString()
//                            purchaseMonth = if (monthOfYear + 1 < 10) {
//                                "0" + (monthOfYear + 1).toString()
//                            } else {
//                                (monthOfYear + 1).toString()
//                            }
//                            purchaseDay = if (dayOfMonth < 10) {
//                                "0$dayOfMonth"
//                            } else {
//                                dayOfMonth.toString()
//                            }
//                            uiHandler.sendEmptyMessage(MSG.PURCHASE_DATE_DATA_INPUT)
//                        },
//                        currentYear,
//                        currentMonth,
//                        currentDay)
//                datePickerDialog.show()
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
                if(etPurchasePrice.hasFocus()){
                    etPurchasePrice.setText(result)
                    etPurchasePrice.setSelection(result.length) //커서를 오른쪽 끝으로 보낸다.
                }else if(etSellPrice.hasFocus()){
                    etSellPrice.setText(result)
                    etSellPrice.setSelection(result.length)
                }
            }
            if(charSequence?.length!! == 0 ){
                if(etPurchasePrice.hasFocus()){
                    txtPurchasePriceSymbol.setTextColor(context.getColor(R.color.color_666666))
                }else if(etSellPrice.hasFocus()){
                    txtSellPriceSymbol.setTextColor(context.getColor(R.color.color_666666))
                }
            }else{
                if(etPurchasePrice.hasFocus()){
                    txtPurchasePriceSymbol.setTextColor(context.getColor(R.color.color_222222))
                }else if(etSellPrice.hasFocus()){
                    txtSellPriceSymbol.setTextColor(context.getColor(R.color.color_222222))
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
    val uiHandler = Handler(Looper.getMainLooper(), UIHandler())
    private inner class UIHandler: Handler.Callback {
        override fun handleMessage(msg: Message): Boolean {
            when (msg.what){
                MSG.PURCHASE_DATE_DATA_INPUT -> {
                    etSellDate.setText("$purchaseYear.$purchaseMonth")
                }
                MSG.SELL_DATE_DATA_INPUT -> {
                    etSellDate.setText("$sellYear.$sellMonth.$sellDay")
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
//        purchaseDay = if (dayOfMonth < 10) {
//            "0$dayOfMonth"
//        } else {
//            dayOfMonth.toString()
//        }
        uiHandler.sendEmptyMessage(MSG.PURCHASE_DATE_DATA_INPUT)
    }
}