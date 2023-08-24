package com.yjpapp.stockportfolio.ui.mystock.dialog

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
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.ibotta.android.support.pickerdialogs.SupportedDatePickerDialog
import com.yjpapp.stockportfolio.data.localdb.room.mystock.MyStockEntity
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.ui.common.dialog.CommonDatePickerDialog
import com.yjpapp.stockportfolio.databinding.CustomDialogInputSellMyStockBinding
import com.yjpapp.stockportfolio.extension.setOnSingleClickListener
import com.yjpapp.stockportfolio.util.DisplayUtils
import com.yjpapp.stockportfolio.util.StockUtils
import es.dmoral.toasty.Toasty


class MyStockSellInputDialog(
    private val mContext: Context,
    private var dialogData: MyStockSellInputDialogData = MyStockSellInputDialogData(),
    private val callBack: CallBack
) : DialogFragment(),
    SupportedDatePickerDialog.OnDateSetListener {
    object MSG {
        const val SELL_DATE_DATA_INPUT: Int = 0
    }

    private var _binding: CustomDialogInputSellMyStockBinding? = null
    private val binding get() = _binding!!

    private var sellYear = ""
    private var sellMonth = ""
    private var sellDay = ""
    private var convertText = ""

    data class MyStockSellInputDialogData(
        var id: Int = -1,
        var sellDate: String = "",
        var sellPrice: String = "",
        var sellCount: String = "",
        var myStockEntity: MyStockEntity = MyStockEntity()
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CustomDialogInputSellMyStockBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        DisplayUtils.setDialogWidthResize(mContext, dialog, 0.85f)
        initData()
        initView()
    }

    private fun initData() {
        dialogData?.let {

        }
    }

    private fun initView() {
        binding.apply {
            etSellDate.setOnSingleClickListener {
                var year = ""
                var month = ""
                var day = ""
                if (etSellDate.text.toString() != "") {
                    val split = etSellDate.text.toString().split("-")
                    year = split[0]
                    month = split[1]
                    day = split[2]
                }
                //매수 날짜 선택 다이얼로그 show
                CommonDatePickerDialog(mContext, year, month, day).apply {
                    setListener { _: DatePicker?, year, month, dayOfMonth ->
                        val todaySplit = StockUtils.getTodayYYYY_MM_DD().split(".")
                        if (year == todaySplit[0].toInt() && month > todaySplit[1].toInt()) {
                            Toasty.error(mContext, "선택하신 월이 현재 보다 큽니다.", Toasty.LENGTH_LONG).show()
                            return@setListener
                        }
                        if (year == todaySplit[0].toInt() && month == todaySplit[1].toInt() && dayOfMonth > todaySplit[2].toInt()) {
                            Toasty.error(mContext, "선택하신 일이 현재 보다 큽니다.", Toasty.LENGTH_LONG).show()
                            return@setListener
                        }
                        uiHandler.sendEmptyMessage(MSG.SELL_DATE_DATA_INPUT)
                        sellYear = year.toString()
                        sellMonth = if (month < 10) {
                            "0$month"
                        } else {
                            month.toString()
                        }
                        sellDay = if (dayOfMonth < 10) {
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
                val sellDate: String = etSellDate.text.toString()
                val sellPrice: String = etSellPrice.text.toString()
                val sellCount: String = etSellCount.text.toString()

                if (sellCount.isEmpty() ||
                    sellDate.isEmpty() ||
                    sellPrice.isEmpty()
                ) {
                    Toasty.error(
                        mContext,
                        mContext.getString(R.string.MyStockInputDialog_Error_Message),
                        Toasty.LENGTH_SHORT
                    ).show()
                    return@setOnSingleClickListener
                }
                if (sellCount.toInt() == 0) {
                    Toasty.error(
                        mContext,
                        mContext.getString(R.string.MyStockInputDialog_Error_Message_Sell_Count),
                        Toasty.LENGTH_SHORT
                    ).show()
                    return@setOnSingleClickListener
                }

                if (dialogData.myStockEntity.purchaseCount < sellCount.toInt()) {
                    Toasty.error(
                        mContext,
                        mContext.getString(R.string.MyStockInputDialog_Error_Message_Sell_Count_Over),
                        Toasty.LENGTH_SHORT
                    ).show()
                    return@setOnSingleClickListener
                }
                callBack.onInputDialogCompleteClicked(
                    dialog = this@MyStockSellInputDialog,
                    userInputDialogData = MyStockSellInputDialogData(
                        sellDate = sellDate,
                        sellPrice = sellPrice,
                        sellCount = sellCount,
                    )
                )
            }
            etSellPrice.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (!TextUtils.isEmpty(s.toString()) && s.toString() != this@MyStockSellInputDialog.convertText) {
                        this@MyStockSellInputDialog.convertText =
                            StockUtils.getNumInsertComma(s.toString())
                        etSellPrice.setText(this@MyStockSellInputDialog.convertText)
                        etSellPrice.setSelection(this@MyStockSellInputDialog.convertText.length) //커서를 오른쪽 끝으로 보낸다.
                    }
                    if (s.isEmpty()) {
                        txtSellPriceSymbol.setTextColor(mContext.getColor(R.color.color_666666))
                    } else {
                        txtSellPriceSymbol.setTextColor(mContext.getColor(R.color.color_222222))
                    }
                }

                override fun afterTextChanged(p0: Editable?) {}
            })
            etSellCount.addTextChangedListener(object : TextWatcher {
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
                    binding.etSellDate.setText("$sellYear-$sellMonth-$sellDay")
                }
            }
            return true
        }
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        sellYear = year.toString()
        sellMonth = if (month + 1 < 10) {
            "0" + (month + 1).toString()
        } else {
            (month + 1).toString()
        }
        sellDay = if (dayOfMonth + 1 < 10) {
            "0" + (dayOfMonth + 1).toString()
        } else {
            (dayOfMonth + 1).toString()
        }
    }

    interface CallBack {
        fun onInputDialogCompleteClicked(
            dialog: MyStockSellInputDialog,
            userInputDialogData: MyStockSellInputDialogData
        )
    }
}