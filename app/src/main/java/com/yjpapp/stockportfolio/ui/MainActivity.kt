package com.yjpapp.stockportfolio.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.database.DatabaseController
import com.yjpapp.stockportfolio.model.DataInfo
import com.yjpapp.stockportfolio.ui.dialog.MainAddListDialog
import com.yjpapp.stockportfolio.util.Utils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_add_portfolio.*
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt


class MainActivity : BaseActivity(R.layout.activity_main), MainListAdapter.DBController {

    private var isEditMode: Boolean = false
    private var mainListAdapter: MainListAdapter? = null
    private var dataList: ArrayList<DataInfo?>? = null
    private var modeType: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.logcat(Utils.getTodayYYYYMMDD())
        initLayout()
        bindLayout()
    }

    override fun delete(position: Int) {
        //TODO 삭제 애니메이션, 팝업창,
        var dataList = mainListAdapter?.getDataInfoList()!!
        val id: Int = dataList[position]?.id!!
        DatabaseController.getInstance(mContext).deleteDataInfo(id)

        dataList = DatabaseController.getInstance(mContext).getAllDataInfo()!!
        mainListAdapter?.setDataInfoList(dataList)
        mainListAdapter?.setEditMode(false)
        mainListAdapter?.notifyDataSetChanged()
    }

    override fun edit(position: Int) {

    }

    private fun initLayout(){
        dataList  = DatabaseController.getInstance(mContext).getAllDataInfo()

        val layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        //Scroll item 2 to 20 pixels from the top
        if(dataList?.size != 0){
            layoutManager.scrollToPosition(dataList?.size!!-1)
        }
        recyclerview_MainActivity.layoutManager = layoutManager

        mainListAdapter = MainListAdapter(dataList, this)
        recyclerview_MainActivity.adapter = mainListAdapter
    }

    private val onClickListener = View.OnClickListener { view: View? ->
        when(view?.id){
            R.id.lin_add -> {
                addPortfolioList()
            }

            R.id.txt_MainActivity_Edit -> {
                if(mainListAdapter?.isEditMode()!!)
                    mainListAdapter?.setEditMode(false)
                else
                    mainListAdapter?.setEditMode(true)
                mainListAdapter?.notifyDataSetChanged()
            }
        }
    }

//    private fun insertData() {
//        val dataInfo = DataInfo(0,"엘비세미콘", "10,000",
//            "2020.10.11", "2020.11.11", "10%",
//            "10,000", "11,000", 10)
//        for(i in 0 until 10){
//            DatabaseController.getInstance(mContext).insertData(dataInfo)
//        }
//    }

    private fun bindLayout(){
        //TODO 실현손익 및 퍼센트 Data Bind 작업
        txt_total_realization_gains_losses_data.text = NumberFormat.getCurrencyInstance(Locale.KOREA).format(650000)
        txt_total_realization_gains_losses_percent.text = "35%"
        lin_add.setOnClickListener(onClickListener)
        txt_MainActivity_Edit.setOnClickListener(onClickListener)
    }

    private fun addPortfolioList(){
        val mainAddListDialog = MainAddListDialog(mContext!!)
        mainAddListDialog.show()
        mainAddListDialog.txt_complete.setOnClickListener {
            //예외처리 (값을 모두 입력하지 않았을 때)
            if (mainAddListDialog.et_subject_name.text.isEmpty() ||
                mainAddListDialog.et_purchase_date.text.isEmpty() ||
                mainAddListDialog.et_sell_date.text.isEmpty() ||
                mainAddListDialog.et_purchase_price.text.isEmpty() ||
                mainAddListDialog.et_sell_price.text.isEmpty() ||
                mainAddListDialog.et_sell_count.text.isEmpty()
            ) {
                Toast.makeText(mContext, "값을 모두 입력해야합니다.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            //매매한 회사이름
            val subjectName = mainAddListDialog.et_subject_name.text.toString()
            //매수일
            val purchaseDate = mainAddListDialog.et_purchase_date.text.toString()
            //매도일
            val sellDate = mainAddListDialog.et_sell_date.text.toString()
            //매수금액
            val purchasePrice = mainAddListDialog.et_purchase_price.text.toString()
            var purchasePriceNumber = ""
            val etPurchasePriceSplit = mainAddListDialog.et_purchase_price.text.split(",")
            for (i in etPurchasePriceSplit.indices) {
                purchasePriceNumber += etPurchasePriceSplit[i]
            }
            //매도금액
            val sellPrice = mainAddListDialog.et_sell_price.text.toString()
            var sellPriceNumber = ""
            val etSellPriceSplit = mainAddListDialog.et_sell_price.text.split(",")
            for (i in etSellPriceSplit.indices) {
                sellPriceNumber += etSellPriceSplit[i]
            }
            //매도수량
            val sellCount = mainAddListDialog.et_sell_count.text.toString().toInt()
            //수익
            val realPainLossesAmountNumber =
                ((sellPriceNumber.toDouble() - purchasePriceNumber.toDouble()) * sellCount).toDouble()
            val realPainLossesAmount = DecimalFormat("###,###").format(realPainLossesAmountNumber)

            val gainPercentNumber = (((sellPriceNumber.toDouble() / purchasePriceNumber.toDouble()) -1) * 100)
            val gainPercent = String.format("%.2f", gainPercentNumber) + "%"

            //날짜오류 예외처리
            if (purchaseDate.toInt() > sellDate.toInt()) {
                Toast.makeText(mContext, "매도한 날짜가 매수한 날짜보다 앞서있습니다.", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }

            if (mainAddListDialog.isShowing) {
                mainAddListDialog.dismiss()
            }
            val dataInfo = DataInfo(0, subjectName, realPainLossesAmount, purchaseDate,
                sellDate, gainPercent, purchasePrice, sellPrice, sellCount)

            DatabaseController.getInstance(mContext).insertData(dataInfo)
            val newDataInfo = DatabaseController.getInstance(mContext).getAllDataInfo()
            mainListAdapter?.setDataInfoList(newDataInfo!!)
            mainListAdapter?.notifyDataSetChanged()
            recyclerview_MainActivity.scrollToPosition(newDataInfo?.size!! - 1)
        }
    }
}