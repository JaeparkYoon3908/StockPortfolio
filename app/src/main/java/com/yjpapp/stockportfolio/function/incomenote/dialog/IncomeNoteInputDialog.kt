package com.yjpapp.stockportfolio.function.incomenote.dialog

import androidx.appcompat.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.DatePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.ibotta.android.support.pickerdialogs.SupportedDatePickerDialog
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.common.StockConfig
import com.yjpapp.stockportfolio.databinding.CustomDialogInputIncomeNoteBinding
import com.yjpapp.stockportfolio.common.dialog.CommonDatePickerDialog
import com.yjpapp.stockportfolio.extension.OnSingleClickListener
import com.yjpapp.stockportfolio.model.request.ReqIncomeNoteInfo
import com.yjpapp.stockportfolio.util.Utils


class IncomeNoteInputDialog(
    private val mContext: Context,
    private val callBack: CallBack,
    private var data: IncomeNoteInputDialogData? = null
) : AlertDialog(mContext), SupportedDatePickerDialog.OnDateSetListener {
    object MSG {
        const val PURCHASE_DATE_DATA_INPUT: Int = 0
        const val SELL_DATE_DATA_INPUT: Int = 1
    }

    data class IncomeNoteInputDialogData(
        var subjectName: String = "",
        var sellDate: String = "",
        var purchasePrice: String = "",
        var sellPrice: String = "",
        var sellCount: String = ""
    )

    private var _binding: CustomDialogInputIncomeNoteBinding? = null
    private val binding get() = _binding!!

    private var purchaseYear: String? = null
    private var purchaseMonth: String? = null
    private var purchaseDay = "01"
    private var sellYear: String? = null
    private var sellMonth: String? = null
    private var sellDay: String = "01"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.custom_dialog_input_income_note,
            null,
            false
        )
        setContentView(binding.root)
        initLayout()
    }

    private fun initLayout() {
        binding.apply {
            etSellDate.setOnClickListener(onClickListener)
            txtCancel.setOnClickListener(onClickListener)
            txtComplete.setOnClickListener(onClickListener)
            EditMainDialogMainContainer.setOnClickListener(onClickListener)

            etPurchasePrice.addTextChangedListener(textWatcher)
            etSellPrice.addTextChangedListener(textWatcher)

            txtPurchasePriceSymbol.text = StockConfig.moneySymbol
            txtSellPriceSymbol.text = StockConfig.moneySymbol

            data = this@IncomeNoteInputDialog.data
        }

        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        Utils.setDialogWidthResize(mContext, this, 0.85f)
    }

    private val onClickListener = OnSingleClickListener { view: View? ->
        when (view?.id) {
            binding.txtCancel.id -> {
                dismiss()
            }
            binding.txtComplete.id -> {
                //예외처리 (값을 모두 입력하지 않았을 때)

                if (binding.etSubjectName.text!!.isEmpty() ||
                    binding.etSellDate.text!!.isEmpty() ||
                    binding.etPurchasePrice.text!!.isEmpty() ||
                    binding.etSellPrice.text!!.isEmpty() ||
                    binding.etSellCount.text!!.isEmpty()
                ) {
                    Toast.makeText(
                        mContext,
                        context.getString(R.string.EditIncomeNoteDialog_Error_Message),
                        Toast.LENGTH_LONG
                    ).show()
                    return@OnSingleClickListener
                }
                if (binding.etPurchasePrice.text.toString() == "0" || binding.etSellPrice.text.toString() == "0") {
                    Toast.makeText(
                        mContext,
                        context.getString(R.string.EditIncomeNoteDialog_Error_Message_Zero),
                        Toast.LENGTH_LONG
                    ).show()
                    return@OnSingleClickListener
                }

                //매매한 회사이름
                val subjectName = binding.etSubjectName.text.toString()
                //매도일
                val sellDate = binding.etSellDate.text.toString()
                //매수금액
                val purchasePrice = binding.etPurchasePrice.text.toString()
                val purchasePriceNumber = Utils.getNumDeletedComma(purchasePrice).toDouble()
                //매도금액
                val sellPrice = binding.etSellPrice.text.toString()
                val sellPriceNumber = Utils.getNumDeletedComma(sellPrice).toDouble()
                //매도수량
                val sellCount = binding.etSellCount.text.toString().toInt()

                val dataInfo = ReqIncomeNoteInfo(
                    id = 0,
                    subjectName = subjectName,
                    sellDate = sellDate,
                    purchasePrice = purchasePriceNumber,
                    sellPrice = sellPriceNumber,
                    sellCount = sellCount
                )
                callBack.onInputDialogCompleteClicked(dataInfo)
                dismiss()
            }
            binding.etSellDate.id -> {
                var year = ""
                var month = ""
                var day = ""
                if (binding.etSellDate.text.toString() != "") {
                    val split = binding.etSellDate.text.toString().split("-")
                    year = split[0]
                    month = split[1]
                    day = split[2]
                }

                CommonDatePickerDialog(mContext, year, month, day).apply {
                    setListener { _: DatePicker?, year, month, dayOfMonth ->
//                        Toast.makeText(requireContext(), "날짜 : $year/$month/$dayOfMonth", Toast.LENGTH_LONG).show()
                        uiHandler.sendEmptyMessage(IncomeNoteInputDialog.MSG.PURCHASE_DATE_DATA_INPUT)
                        purchaseYear = year.toString()
                        purchaseMonth = if (month < 10) {
                            "0$month"
                        } else {
                            month.toString()
                        }
                        purchaseDay = if (dayOfMonth < 10) {
                            "0$dayOfMonth"
                        } else {
                            dayOfMonth.toString()
                        }
                        dismiss()
                    }
                    show()
                }
            }
        }
    }

    //3자리마다 콤마 찍어주는 코드
    private var convertText = ""
    private val textWatcher = object : TextWatcher {
        override fun onTextChanged(
            charSequence: CharSequence?,
            start: Int,
            before: Int,
            count: Int
        ) {
            if (!TextUtils.isEmpty(charSequence.toString()) && charSequence.toString() != convertText) {
                convertText = Utils.getNumInsertComma(charSequence.toString())
                if (binding.etPurchasePrice.hasFocus()) {
                    binding.etPurchasePrice.setText(convertText)
                    binding.etPurchasePrice.setSelection(convertText.length) //커서를 오른쪽 끝으로 보낸다.
                } else if (binding.etSellPrice.hasFocus()) {
                    binding.etSellPrice.setText(convertText)
                    binding.etSellPrice.setSelection(convertText.length)
                }
            }
            if (charSequence?.length!! == 0) {
                if (binding.etPurchasePrice.hasFocus()) {
                    binding.txtPurchasePriceSymbol.setTextColor(context.getColor(R.color.color_666666))
                } else if (binding.etSellPrice.hasFocus()) {
                    binding.txtSellPriceSymbol.setTextColor(context.getColor(R.color.color_666666))
                }
            } else {
                if (binding.etPurchasePrice.hasFocus()) {
                    binding.txtPurchasePriceSymbol.setTextColor(context.getColor(R.color.color_222222))
                } else if (binding.etSellPrice.hasFocus()) {
                    binding.txtSellPriceSymbol.setTextColor(context.getColor(R.color.color_222222))
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

    private inner class UIHandler : Handler.Callback {
        override fun handleMessage(msg: Message): Boolean {
            when (msg.what) {
                MSG.PURCHASE_DATE_DATA_INPUT -> {
                    binding.etSellDate.setText("$purchaseYear-$purchaseMonth-$purchaseDay")
                }
                MSG.SELL_DATE_DATA_INPUT -> {
                    binding.etSellDate.setText("$sellYear-$sellMonth-$sellDay")
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

    interface CallBack {
        fun onInputDialogCompleteClicked(reqIncomeNoteInfo: ReqIncomeNoteInfo)
    }
}