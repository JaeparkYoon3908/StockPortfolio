package com.yjpapp.stockportfolio.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.model.DataInfo
import com.yjpapp.stockportfolio.ui.dialog.EditMainListDialog
import com.yjpapp.stockportfolio.ui.dialog.MainFilterDialog
import com.yjpapp.stockportfolio.util.Utils
import jp.wasabeef.recyclerview.animators.FadeInAnimator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dailog_main_filter.*
import kotlinx.android.synthetic.main.dialog_add_portfolio.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*


class MainActivity : BaseActivity(R.layout.activity_main), MainListAdapter.DBController {

    private var mainListAdapter: MainListAdapter? = null
    private var allDataList: ArrayList<DataInfo?>? = null
    private var insertMode: Boolean = false
    private var editSelectPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLayout()
    }

    private fun addPortfolioList(){
        val editMainListDialog = EditMainListDialog(mContext)
        if(!editMainListDialog.isShowing){
            editMainListDialog.show()
            editMainListDialog.txt_complete.setOnClickListener {
                runDialogCompleteClick(editMainListDialog)
            }
        }
    }

    override fun deletePortfolioList(position: Int) {
        var dataList = mainListAdapter?.getDataInfoList()!!
        val id: Int = dataList[position]?.id!!
        databaseController.deleteDataInfo(id)

        dataList = databaseController.getAllDataInfo()!!
        mainListAdapter?.setDataInfoList(dataList)
        mainListAdapter?.setEditMode(false)
        mainListAdapter?.notifyItemRemoved(position)
        bindTotalGainData()
    }

    override fun editPortfolioList(position: Int) {
        insertMode = false
        editSelectPosition = position
        val editMainListDialog = EditMainListDialog(mContext)
        if(!editMainListDialog.isShowing){
            editMainListDialog.show()
            editMainListDialog.et_subject_name.setText(allDataList!![position]?.subjectName)
            editMainListDialog.et_purchase_date.setText(allDataList!![position]?.purchaseDate)
            editMainListDialog.et_sell_date.setText(allDataList!![position]?.sellDate)
            editMainListDialog.et_purchase_price.setText(allDataList!![position]?.purchasePrice)
            editMainListDialog.et_sell_price.setText(allDataList!![position]?.sellPrice)
            editMainListDialog.et_sell_count.setText(allDataList!![position]?.sellCount.toString())
            editMainListDialog.txt_complete.setOnClickListener {
                runDialogCompleteClick(editMainListDialog)
            }
        }
    }

    override fun onBackPressed() {
        if(mainListAdapter?.isEditMode()!!){
            mainListAdapter?.setEditMode(false)
            mainListAdapter?.notifyDataSetChanged()
        }else{
            finish()
        }
    }

    private fun initLayout(){
        bindTotalGainData()

        lin_add.setOnClickListener(onClickListener)
        lin_MainActivity_Filter.setOnClickListener(onClickListener)
        txt_MainActivity_Edit.setOnClickListener(onClickListener)

        initRecyclerView()
    }

    private fun initRecyclerView(){
        val layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        //Scroll item 2 to 20 pixels from the top
        if(allDataList?.size != 0){
            layoutManager.scrollToPosition(allDataList?.size!!-1)
        }
        recyclerview_MainActivity.layoutManager = layoutManager

        mainListAdapter = MainListAdapter(allDataList, this)
        recyclerview_MainActivity.adapter = mainListAdapter
        recyclerview_MainActivity.itemAnimator = FadeInAnimator()

    }

    private val onClickListener = View.OnClickListener { view: View? ->
        when(view?.id){
            R.id.lin_add -> {
                insertMode = true
                addPortfolioList()
            }

            R.id.txt_MainActivity_Edit -> {
                if(mainListAdapter?.isEditMode()!!)
                    mainListAdapter?.setEditMode(false)
                else
                    mainListAdapter?.setEditMode(true)
                mainListAdapter?.notifyDataSetChanged()
            }
            R.id.lin_MainActivity_Filter -> {
                initFilterDialog()
            }
        }
    }

    private fun runDialogCompleteClick(editMainListDialog: EditMainListDialog){
        //예외처리 (값을 모두 입력하지 않았을 때)
        if (editMainListDialog.et_subject_name.text.isEmpty() ||
            editMainListDialog.et_purchase_date.text.isEmpty() ||
            editMainListDialog.et_sell_date.text.isEmpty() ||
            editMainListDialog.et_purchase_price.text.isEmpty() ||
            editMainListDialog.et_sell_price.text.isEmpty() ||
            editMainListDialog.et_sell_count.text.isEmpty()) {

            Toast.makeText(mContext, "값을 모두 입력해야합니다.", Toast.LENGTH_LONG).show()
            return
        }

        //매매한 회사이름
        val subjectName = editMainListDialog.et_subject_name.text.toString()
        //매수일
        val purchaseDate = editMainListDialog.et_purchase_date.text.toString()
        //매도일
        val sellDate = editMainListDialog.et_sell_date.text.toString()
        //매수금액
        val purchasePrice = editMainListDialog.et_purchase_price.text.toString()
        val purchasePriceNumber = Utils.getNumDeletedComma(purchasePrice)
        //매도금액
        val sellPrice = editMainListDialog.et_sell_price.text.toString()
        val sellPriceNumber = Utils.getNumDeletedComma(sellPrice)
        //매도수량
        val sellCount = editMainListDialog.et_sell_count.text.toString().toInt()
        //수익
        val realPainLossesAmountNumber =
            ((sellPriceNumber.toDouble() - purchasePriceNumber.toDouble()) * sellCount)
        val realPainLossesAmount = DecimalFormat("###,###").format(realPainLossesAmountNumber)
        //수익률
        val gainPercentNumber = Utils.calculateGainPercent(purchasePrice, sellPrice)
        val gainPercent = Utils.getRoundsPercentNumber(gainPercentNumber)

        //날짜오류 예외처리
        if (Utils.getNumDeletedDot(purchaseDate).toInt() > Utils.getNumDeletedDot(sellDate).toInt()) {
            Toast.makeText(mContext, "매도한 날짜가 매수한 날짜보다 앞서있습니다.", Toast.LENGTH_LONG)
                .show()
            return
        }

        if (editMainListDialog.isShowing) {
            editMainListDialog.dismiss()
        }
        val dataInfo = DataInfo(0, subjectName, realPainLossesAmount, purchaseDate,
            sellDate, gainPercent, purchasePrice, sellPrice, sellCount)

        if(insertMode){
            databaseController.insertData(dataInfo)
        }else{
            dataInfo.id = allDataList!![editSelectPosition]!!.id
            databaseController.updateData(dataInfo)
        }
        val newDataInfo = databaseController.getAllDataInfo()
        mainListAdapter?.setDataInfoList(newDataInfo!!)
        mainListAdapter?.setEditMode(false)
        if(insertMode){
            mainListAdapter?.notifyItemInserted(mainListAdapter?.itemCount!!-1)
        }else{
            mainListAdapter?.notifyDataSetChanged()
        }
        recyclerview_MainActivity.scrollToPosition(newDataInfo?.size!! - 1)
        bindTotalGainData()
    }
    private fun bindTotalGainData(){
        allDataList  = databaseController.getAllDataInfo()
        var totalGainNumber: Double = 0.0
        var totalGainPercent: Double = 0.0
        for(i in allDataList?.indices!!){
            totalGainNumber += Utils.getNumDeletedComma(allDataList!![i]!!.realPainLossesAmount!!).toDouble()
            totalGainPercent += Utils.getNumDeletedPercent(allDataList!![i]!!.gainPercent!!).toDouble()
        }
        totalGainPercent /= allDataList!!.size
        txt_total_realization_gains_losses_data.text = NumberFormat.getCurrencyInstance(Locale.KOREA).format(totalGainNumber)
        txt_total_realization_gains_losses_percent.text = Utils.getRoundsPercentNumber(totalGainPercent)
    }
    private fun initFilterDialog(){
        val mainFilterDialog = MainFilterDialog(mContext)
        mainFilterDialog.show()
        mainFilterDialog.txt_MainFilterDialog_all.setOnClickListener {
            allDataFiltering()
            mainFilterDialog.dismiss()
        }
        mainFilterDialog.txt_MainFilterDialog_gain.setOnClickListener {
            gainDataFiltering()
            mainFilterDialog.dismiss()
        }
        mainFilterDialog.txt_MainFilterDialog_loss.setOnClickListener {
            lossDataFiltering()
            mainFilterDialog.dismiss()
        }
    }
    private fun allDataFiltering(){
        allDataList = databaseController.getAllDataInfo()
        mainListAdapter?.setDataInfoList(allDataList!!)
        mainListAdapter?.notifyDataSetChanged()
        txt_MainActivity_Filter.text = getString(R.string.MainFilterDialog_All)
    }

    private fun gainDataFiltering(){
        val gainDataList = databaseController.getGainDataInfo()
        mainListAdapter?.setDataInfoList(gainDataList!!)
        mainListAdapter?.notifyDataSetChanged()
        txt_MainActivity_Filter.text = getString(R.string.MainFilterDialog_Gain)
    }

    private fun lossDataFiltering(){
        val lossDataList = databaseController.getLossDataInfo()
        mainListAdapter?.setDataInfoList(lossDataList!!)
        mainListAdapter?.notifyDataSetChanged()
        txt_MainActivity_Filter.text = getString(R.string.MainFilterDialog_Loss)
    }
}