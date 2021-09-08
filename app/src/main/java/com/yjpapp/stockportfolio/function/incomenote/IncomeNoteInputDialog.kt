package com.yjpapp.stockportfolio.function.incomenote

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
import com.yjpapp.stockportfolio.constance.StockPortfolioConfig
import com.yjpapp.stockportfolio.model.request.ReqIncomeNoteInfo
import com.yjpapp.stockportfolio.model.response.RespIncomeNoteInfo
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
    var purchaseDay = "01"
    var sellYear: String? = null
    var sellMonth: String? = null
    var sellDay: String = "01"
    val etSellDate: EditText by lazy { findViewById(R.id.et_sell_date) }
    val txtCancel: TextView by lazy { findViewById(R.id.txt_cancel) }
    val txtComplete: TextView by lazy { findViewById(R.id.txt_complete) }
    val etPurchasePrice:EditText by lazy { findViewById(R.id.et_purchase_price) }
    private val EditMainDialogMainContainer:ConstraintLayout by lazy { findViewById(R.id.EditMainDialog_MainContainer) }
    val etSellPrice: EditText by lazy { findViewById(R.id.et_sell_price) }
    val txtPurchasePriceSymbol: TextView by lazy { findViewById(R.id.txt_purchase_price_symbol) }
    val txtSellPriceSymbol: TextView by lazy { findViewById(R.id.txt_sell_price_symbol) }
    val etSubjectName: EditText by lazy { findViewById(R.id.et_subject_name) }
    val etSellCount: EditText by lazy { findViewById(R.id.et_sell_count) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_input_income_note)
        initLayout()
    }

    private fun initLayout(){
        etSellDate.setOnClickListener(onClickListener)
        txtCancel.setOnClickListener(onClickListener)
        txtComplete.setOnClickListener(onClickListener)
        EditMainDialogMainContainer.setOnClickListener(onClickListener)

        etPurchasePrice.addTextChangedListener(textWatcher)
        etSellPrice.addTextChangedListener(textWatcher)

        txtPurchasePriceSymbol.text = StockPortfolioConfig.moneySymbol
        txtSellPriceSymbol.text = StockPortfolioConfig.moneySymbol

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
                val sellDate = etSellDate.text.toString() + "-01"
                //매수금액
                val purchasePrice = etPurchasePrice.text.toString()
                val purchasePriceNumber = Utils.getNumDeletedComma(purchasePrice).toDouble()
                //매도금액
                val sellPrice = etSellPrice.text.toString()
                val sellPriceNumber = Utils.getNumDeletedComma(sellPrice).toDouble()
                //매도수량
                val sellCount = etSellCount.text.toString().toInt()

                val dataInfo = ReqIncomeNoteInfo(
                    id = 0,
                    subjectName = subjectName,
                    sellDate = sellDate,
                    purchasePrice = purchasePriceNumber,
                    sellPrice = sellPriceNumber,
                    sellCount = sellCount)
                incomeNotePresenter.onInputDialogCompleteClicked(mContext, dataInfo)
                dismiss()
            }
            etSellDate.id -> {

            }
        }
    }

    //3자리마다 콤마 찍어주는 코드
    private var convertText = ""
    private val textWatcher = object: TextWatcher{
        override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
            if(!TextUtils.isEmpty(charSequence.toString()) && charSequence.toString() != convertText){
                convertText = Utils.getNumInsertComma(charSequence.toString())
                if(etPurchasePrice.hasFocus()){
                    etPurchasePrice.setText(convertText)
                    etPurchasePrice.setSelection(convertText.length) //커서를 오른쪽 끝으로 보낸다.
                }else if(etSellPrice.hasFocus()){
                    etSellPrice.setText(convertText)
                    etSellPrice.setSelection(convertText.length)
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
                    etSellDate.setText("$purchaseYear-$purchaseMonth-$purchaseDay")
                }
                MSG.SELL_DATE_DATA_INPUT -> {
                    etSellDate.setText("$sellYear-$sellMonth-$sellDay")
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

        uiHandler.sendEmptyMessage(MSG.PURCHASE_DATE_DATA_INPUT)
    }
}