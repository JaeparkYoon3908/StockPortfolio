package com.yjpapp.stockportfolio.function.mystock.dialog

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
import android.view.WindowManager
import android.widget.DatePicker
import androidx.databinding.DataBindingUtil
import com.ibotta.android.support.pickerdialogs.SupportedDatePickerDialog
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.databinding.CustomDialogInputMyStockBinding
import com.yjpapp.stockportfolio.common.dialog.CommonDatePickerDialog
import com.yjpapp.stockportfolio.extension.setOnSingleClickListener
import com.yjpapp.stockportfolio.function.incomenote.dialog.IncomeNoteInputDialog
import com.yjpapp.stockportfolio.util.Utils
import es.dmoral.toasty.Toasty


class MyStockInputDialog(
    private val mContext: Context,
    private var myStockInputDialogData: MyStockInputDialogData? = MyStockInputDialogData(),
    private val callBack: CallBack
) : AlertDialog(mContext),
    SupportedDatePickerDialog.OnDateSetListener
{

    object MSG {
        const val SELL_DATE_DATA_INPUT: Int = 0
    }

    private var _binding: CustomDialogInputMyStockBinding? = null
    private val binding get() = _binding!!

    private var purchaseYear = ""
    private var purchaseMonth = ""
    private var purchaseDay = ""
    private var convertText = ""
    data class MyStockInputDialogData(
        var id: Int = -1,
        var subjectName: String = "",
        var purchaseDate: String = "",
        var purchasePrice: String = "",
        var purchaseCount: String = "",
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.custom_dialog_input_my_stock,
            null,
            false
        )
        setContentView(binding.root)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        //EditText focus 했을 때 키보드가 보이도록 설정
        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        Utils.setDialogWidthResize(mContext, this, 0.85f)
        initData()
        initView()
    }

    private fun initData() {
        binding.data = myStockInputDialogData
    }

    private fun initView() {
        binding.apply {
            etSubjectName.setOnSingleClickListener {
//                    val intent = Intent(mContext, StockSearchActivity::class.java)
//                    startActivityForResult(intent, 10000)
            }
            etPurchaseDate.setOnSingleClickListener {
                var year = ""
                var month = ""
                var day = ""
                if (binding.etPurchaseDate.text.toString() != "") {
                    val split = binding.etPurchaseDate.text.toString().split("-")
                    year = split[0]
                    month = split[1]
                    day = split[2]
                }
                //매수 날짜 선택 다이얼로그 show
                CommonDatePickerDialog(mContext, year, month, day).apply {
                    setListener { _: DatePicker?, year, month, dayOfMonth ->
                        val todaySplit = Utils.getTodayYYYY_MM_DD().split(".")
                        if (year > todaySplit[0].toInt()) {
                            Toasty.error(mContext, "선택하신 연도가 현재 보다 큽니다.", Toasty.LENGTH_LONG).show()
                            return@setListener
                        }
                        if (month > todaySplit[1].toInt()) {
                            Toasty.error(mContext, "선택하신 월이 현재 보다 큽니다.", Toasty.LENGTH_LONG).show()
                            return@setListener
                        }
                        if (dayOfMonth > todaySplit[2].toInt()) {
                            Toasty.error(mContext, "선택하신 일이 현재 보다 큽니다.", Toasty.LENGTH_LONG).show()
                            return@setListener
                        }
                        uiHandler.sendEmptyMessage(MSG.SELL_DATE_DATA_INPUT)
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
            txtCancel.setOnSingleClickListener { dismiss() }
            txtComplete.setOnSingleClickListener {
                val subjectName: String = binding.etSubjectName.text.toString()
                val purchaseDate: String = binding.etPurchaseDate.text.toString()
                val purchasePrice: String = binding.etPurchasePrice.text.toString()
                val purchaseCount: String = binding.etPurchaseCount.text.toString()

                if (purchaseCount.isEmpty() ||
                    purchaseDate.isEmpty() ||
                    purchasePrice.isEmpty() ||
                    subjectName.isEmpty()
                ){
                    Toasty.error(mContext, mContext.getString(R.string.MyStockInputDialog_Error_Message), Toasty.LENGTH_SHORT).show()
                    return@setOnSingleClickListener
                }
                callBack.onInputDialogCompleteClicked(
                    this@MyStockInputDialog,
                    MyStockInputDialogData(
                    subjectName = subjectName,
                    purchaseDate = purchaseDate,
                    purchasePrice = purchasePrice,
                    purchaseCount = purchaseCount
                ))
            }
            etPurchasePrice.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (!TextUtils.isEmpty(s.toString()) && s.toString() != this@MyStockInputDialog.convertText) {
                        this@MyStockInputDialog.convertText = Utils.getNumInsertComma(s.toString())
                        binding.etPurchasePrice.setText(this@MyStockInputDialog.convertText)
                        binding.etPurchasePrice.setSelection(this@MyStockInputDialog.convertText.length) //커서를 오른쪽 끝으로 보낸다.
                    }
                    if (s.isEmpty()) {
                        binding.txtPurchasePriceSymbol.setTextColor(mContext.getColor(R.color.color_666666))
                    } else {
                        binding.txtPurchasePriceSymbol.setTextColor(mContext.getColor(R.color.color_222222))
                    }
                }
                override fun afterTextChanged(p0: Editable?) {}
            })
            etPurchaseCount.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                }
                override fun afterTextChanged(p0: Editable?) {}
            })
        }
    }
    private val uiHandler = Handler(Looper.getMainLooper(), UIHandler())
    private inner class UIHandler : Handler.Callback {
        override fun handleMessage(msg: Message): Boolean {
            when (msg.what) {
                MSG.SELL_DATE_DATA_INPUT -> {
                    binding.etPurchaseDate.setText("$purchaseYear-$purchaseMonth-$purchaseDay")
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
        purchaseDay = if (dayOfMonth + 1 < 10) {
            "0" + (dayOfMonth + 1).toString()
        } else {
            (dayOfMonth + 1).toString()
        }
        uiHandler.sendEmptyMessage(IncomeNoteInputDialog.MSG.PURCHASE_DATE_DATA_INPUT)
    }

    private fun onCancelButtonClick() {
        dismiss()
    }

    interface CallBack {
        fun onInputDialogCompleteClicked(dialog: MyStockInputDialog, userInputDialogData: MyStockInputDialogData)
    }
}