package com.yjpapp.stockportfolio.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.model.DataInfo
import com.yjpapp.stockportfolio.ui.dialog.MainAddListDialog
import com.yjpapp.stockportfolio.util.Utils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_add_portfolio.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : BaseActivity(R.layout.activity_main), MainListAdapter.DBController {

    private var isEditMode: Boolean = false
    private var mainListAdapter: MainListAdapter? = null
    private var dataList: ArrayList<DataInfo?>? = null
    private var modeType: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.logcat(Utils.getTodayYYYYMMDD())
        initLayout()
//        insertData()
    }

    private fun initLayout(){
        val layoutManager = LinearLayoutManager(mContext!!, LinearLayoutManager.VERTICAL, false)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        //Scroll item 2 to 20 pixels from the top
        layoutManager.scrollToPositionWithOffset(2,20)
        recyclerview_MainActivity.layoutManager = layoutManager

        dataList  = databaseController?.getAllDataInfo()
        mainListAdapter = MainListAdapter(dataList, this)

        recyclerview_MainActivity.adapter = mainListAdapter
        txt_total_realization_gains_losses_data.text = NumberFormat.getCurrencyInstance(Locale.KOREA).format(650000)
        txt_total_realization_gains_losses_percent.text = "35%"
        lin_add.setOnClickListener(onClickListener)
        txt_MainActivity_Edit.setOnClickListener(onClickListener)
    }

    private val onClickListener = View.OnClickListener { view: View? ->
        when(view?.id){
            R.id.lin_add -> {
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

                    val subjectName: String = mainAddListDialog.et_subject_name.text.toString()
                    val purchaseDate: String = mainAddListDialog.et_purchase_date.text.toString()
                    val sellDate: String = mainAddListDialog.et_sell_date.text.toString()

                    val purchasePrice = mainAddListDialog.et_purchase_price.text.toString()
                    var purchasePriceNumber = ""
                    val etPurchasePriceSplit = mainAddListDialog.et_purchase_price.text.split(",")
                    for (i in etPurchasePriceSplit.indices) {
                        purchasePriceNumber += etPurchasePriceSplit[i]
                    }
                    val sellPrice = mainAddListDialog.et_sell_price.text.toString()
                    var sellPriceNumber = ""
                    val etSellPriceSplit = mainAddListDialog.et_sell_price.text.split(",")
                    for (i in etSellPriceSplit.indices) {
                        sellPriceNumber += etSellPriceSplit[i]
                    }
                    val sellCount: Int = mainAddListDialog.et_sell_count.text.toString().toInt()
                    val realPainLossesAmount: String =
                        ((sellPriceNumber.toInt() - purchasePriceNumber.toInt()) * sellCount).toString()
                    //TODO 수익률 계산 제대로 하기.
                    val gainPercent: String =
                        ((sellPriceNumber.toInt() / purchasePriceNumber.toInt()) * 100).toString() + "%"

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
                    databaseController?.insertData(dataInfo)
                    val newDataInfo = databaseController?.getAllDataInfo()
                    mainListAdapter?.setDataInfoList(newDataInfo!!)
                    mainListAdapter?.notifyDataSetChanged()
                    recyclerview_MainActivity.scrollToPosition(newDataInfo?.size!! - 1)
                }
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
    override fun delete(position: Int) {
        //TODO 삭제 애니메이션, 팝업창,
        var dataList = mainListAdapter?.getDataInfoList()!!
        val id: Int = dataList[position]?.id!!
        databaseController?.deleteDataInfo(id)

        dataList = databaseController?.getAllDataInfo()!!
        mainListAdapter?.setDataInfoList(dataList)
        mainListAdapter?.setEditMode(false)
        mainListAdapter?.notifyDataSetChanged()
    }

    override fun edit(position: Int) {

    }


    private fun insertData() {
        val dataInfo = DataInfo(0,"엘비세미콘", "10,000",
            "2020.10.11", "2020.11.11", "10%",
            "10,000", "11,000", 10)
        for(i in 0 until 10){
            databaseController?.insertData(dataInfo)
        }
    }
}