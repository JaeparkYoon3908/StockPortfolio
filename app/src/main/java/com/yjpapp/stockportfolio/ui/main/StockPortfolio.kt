package com.yjpapp.stockportfolio.ui.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.database.Databases
import com.yjpapp.stockportfolio.database.model.PortfolioInfo
import com.yjpapp.stockportfolio.network.YahooFinanceProtocolManager
import com.yjpapp.stockportfolio.network.model.Price
import com.yjpapp.stockportfolio.ui.BaseActivity
import com.yjpapp.stockportfolio.ui.dialog.EditPortfolioDialog
import com.yjpapp.stockportfolio.ui.dialog.MainFilterDialog
import com.yjpapp.stockportfolio.ui.memo.MemoListActivity
import com.yjpapp.stockportfolio.util.Utils
import es.dmoral.toasty.Toasty
import jp.wasabeef.recyclerview.animators.FadeInAnimator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_add_portfolio.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*


class StockPortfolio : BaseActivity(R.layout.activity_main),
    StockPortfolioListAdapter.MainActivityCallBack, MainFilterDialog.MainFilterClicked {

    private var stockPortfolioListAdapter: StockPortfolioListAdapter? = null
    private var allPortfolioList: ArrayList<PortfolioInfo?>? = null
    private var insertMode: Boolean = false
    private var editSelectPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        initLayout()
//        preferenceController.setPreference(SettingPrefKey.KEY_ViBRATION, "완전세게")
//        logcat(preferenceController.getPreference(SettingPrefKey.KEY_ViBRATION))
    }

    private fun addClicked(){
//        EditPortfolioDialog(mContext).apply {
//            if(!isShowing){
//                show()
//                txt_complete.setOnClickListener {
//                    runDialogCompleteClick(this)
//                }
//                txt_cancel.setOnClickListener {
//                    dismiss()
//                }
//            }
//        }
        val symbol = "005930.KS"
        val region = "KR"
        YahooFinanceProtocolManager.getInstance(mContext).getStockProfile(symbol, region,
            object: Callback<JsonObject?> {
            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                if(response.code() == 200 || response.code() == 204){
                    try {
//                    val formattedResult = StringBuilder()
                        val jsonObject = JSONObject(response.body().toString())
                        val priceJSONArray = jsonObject.getJSONObject("price").toString()
                        val price: Price = Gson().fromJson(priceJSONArray, Price::class.java)
                        Log.d("YJP", "price = $price")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                Log.d("RequestResult", "RetrofitExample, Type : get, Result : onFailure, Error Message : " + t.message)
            }
        })
    }

    override fun onDeleteClicked(position: Int) {
        var dataList = stockPortfolioListAdapter?.getDataInfoList()!!
        val id: Int = dataList[position]?.id!!
        databaseController.deleteData(id, Databases.TABLE_PORTFOLIO)

        dataList = databaseController.getAllPortfolioDataInfo()
        stockPortfolioListAdapter?.setDataInfoList(dataList)
        stockPortfolioListAdapter?.setEditMode(false)
        stockPortfolioListAdapter?.notifyItemRemoved(position)
        addButtonControl()
        bindTotalGainData()
    }

    override fun onEditClicked(position: Int) {
        insertMode = false
        stockPortfolioListAdapter?.setEditMode(false)
        stockPortfolioListAdapter?.notifyDataSetChanged()
        editSelectPosition = position
        EditPortfolioDialog(mContext).apply {
            if(!isShowing){
                show()
                et_subject_name.setText(allPortfolioList!![position]?.subjectName)
                et_purchase_date.setText(allPortfolioList!![position]?.purchaseDate)
                et_sell_date.setText(allPortfolioList!![position]?.sellDate)
                et_purchase_price.setText(allPortfolioList!![position]?.purchasePrice)
                et_sell_price.setText(allPortfolioList!![position]?.sellPrice)
                et_sell_count.setText(allPortfolioList!![position]?.sellCount.toString())
                txt_complete.setOnClickListener {
                    runDialogCompleteClick(this)
                }
                txt_cancel.setOnClickListener {
                    runDialogCancelClick(this)
                }
            }
        }
    }

    override fun onItemLongClicked() {
        addButtonControl()
    }

    private var menu: Menu? = null
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.menu_main, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.menu_MainActivity_Add -> {
                insertMode = true
                addClicked()
            }

//            R.id.menu_MainActivity_Memo -> {
//                val intent = Intent(mContext, MemoListActivity::class.java)
//                startActivity(intent)
//            }
        }
        return super.onOptionsItemSelected(item)
    }

    private var allowAppFinish = false
    override fun onBackPressed() {
        if(stockPortfolioListAdapter?.isEditMode()!!){
            stockPortfolioListAdapter?.setEditMode(false)
            stockPortfolioListAdapter?.notifyDataSetChanged()
        }else{
            if(!allowAppFinish){
//                Toast.makeText(mContext, "앱을 종료하려면 한번 더 눌러주세요.", Toast.LENGTH_SHORT).show()
                Toasty.info(mContext, "앱을 종료하려면 한번 더 눌러주세요.", Toast.LENGTH_SHORT).show()

                allowAppFinish = true
                Handler().postDelayed(Runnable {
                    allowAppFinish = false
                }, 3000)
            }else{
                finishAffinity()
            }
        }
    }

    override fun initData() {
        allPortfolioList  = databaseController.getAllPortfolioDataInfo()
    }
    override fun initLayout(){
        //Toolbar
        setSupportActionBar(toolbar_MainActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        //상단
        bindTotalGainData()

        img_MainActivity_Memo.setOnClickListener(onClickListener)
        lin_MainActivity_Filter.setOnClickListener(onClickListener)
        txt_MainActivity_Edit.setOnClickListener(onClickListener)
        //노트 리스트
        initRecyclerView()
    }

    private fun initRecyclerView(){
        val layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        //Scroll item 2 to 20 pixels from the top
        if(allPortfolioList?.size != 0){
            layoutManager.scrollToPosition(allPortfolioList?.size!! - 1)
        }
        recyclerview_MainActivity.layoutManager = layoutManager

        stockPortfolioListAdapter = StockPortfolioListAdapter(allPortfolioList, this)
        recyclerview_MainActivity.adapter = stockPortfolioListAdapter
        recyclerview_MainActivity.itemAnimator = FadeInAnimator()

    }

    private val onClickListener = View.OnClickListener { view: View? ->
        when(view?.id){
            R.id.img_MainActivity_Memo -> {
                val intent = Intent(mContext, MemoListActivity::class.java)
                startActivity(intent)
            }

            R.id.txt_MainActivity_Edit -> {
                window?.attributes?.windowAnimations = R.style.AnimationPopupStyle
                if (allPortfolioList?.size!! > 0) {
                    stockPortfolioListAdapter?.setEditMode(!stockPortfolioListAdapter?.isEditMode()!!)
                    stockPortfolioListAdapter?.notifyDataSetChanged()
                    addButtonControl()
                }

            }
            R.id.lin_MainActivity_Filter -> {
                MainFilterDialog(this).run {
                    show(supportFragmentManager, tag)
                }
//                initFilterDialog()
            }
        }
    }

    private fun runDialogCompleteClick(editPortfolioDialog: EditPortfolioDialog){

        editPortfolioDialog.run{
            //예외처리 (값을 모두 입력하지 않았을 때)
            if (et_subject_name.text.isEmpty() ||
                et_purchase_date.text.isEmpty() ||
                et_sell_date.text.isEmpty() ||
                et_purchase_price.text.isEmpty() ||
                et_sell_price.text.isEmpty() ||
                et_sell_count.text.isEmpty()) {

                Toast.makeText(mContext, "값을 모두 입력해야합니다.", Toast.LENGTH_LONG).show()
                return
            }

            //매매한 회사이름
            val subjectName = et_subject_name.text.toString()
            //매수일
            val purchaseDate = et_purchase_date.text.toString()
            //매도일
            val sellDate = et_sell_date.text.toString()
            //매수금액
            val purchasePrice = et_purchase_price.text.toString()
            val purchasePriceNumber = Utils.getNumDeletedComma(purchasePrice)
            //매도금액
            val sellPrice = et_sell_price.text.toString()
            val sellPriceNumber = Utils.getNumDeletedComma(sellPrice)
            //매도수량
            val sellCount = et_sell_count.text.toString().toInt()
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

            val dataInfo = PortfolioInfo(0, subjectName, realPainLossesAmount, purchaseDate,
                sellDate, gainPercent, purchasePrice, sellPrice, sellCount)

            if(insertMode){
                databaseController.insertPortfolioData(dataInfo)
            }else{
                dataInfo.id = allPortfolioList!![editSelectPosition]!!.id
                databaseController.updatePortfolioData(dataInfo)
            }
            dismiss()
        }
        val newDataInfo = databaseController.getAllPortfolioDataInfo()
        stockPortfolioListAdapter?.setDataInfoList(newDataInfo)
        if(insertMode){
            stockPortfolioListAdapter?.notifyItemInserted(stockPortfolioListAdapter?.itemCount!! - 1)
        }else{
            stockPortfolioListAdapter?.notifyDataSetChanged()
        }
        recyclerview_MainActivity.scrollToPosition(newDataInfo.size - 1)
        bindTotalGainData()
    }

    private fun runDialogCancelClick(editPortfolioDialog: EditPortfolioDialog){
        editPortfolioDialog.run {
//            mainListAdapter?.setEditMode(false)
//            mainListAdapter?.notifyDataSetChanged()
            addButtonControl()
            dismiss()
        }
    }

    private fun bindTotalGainData(){
        allPortfolioList  = databaseController.getAllPortfolioDataInfo()
        var totalGainNumber: Double = 0.0
        var totalGainPercent: Double = 0.0
        for(i in allPortfolioList?.indices!!){
            totalGainNumber += Utils.getNumDeletedComma(allPortfolioList!![i]!!.realPainLossesAmount!!).toDouble()
            totalGainPercent += Utils.getNumDeletedPercent(allPortfolioList!![i]!!.gainPercent!!).toDouble()
        }
        totalGainPercent /= allPortfolioList!!.size
        txt_total_realization_gains_losses_data.text = NumberFormat.getCurrencyInstance(Locale.KOREA).format(
            totalGainNumber)
        if(totalGainNumber >= 0){
            txt_total_realization_gains_losses_data.setTextColor(getColor(R.color.color_e52b4e))
        }else{
            txt_total_realization_gains_losses_data.setTextColor(getColor(R.color.color_4876c7))
        }
        txt_total_realization_gains_losses_percent.text = Utils.getRoundsPercentNumber(
            totalGainPercent)
    }

    override fun allSelect() {
        allDataFiltering()
    }

    override fun gainSelect() {
        gainDataFiltering()
    }

    override fun lossSelect() {
        lossDataFiltering()
    }

    private fun initFilterDialog(){
//        MaiFilternDialog.getInstance(mContext).apply {
//            show(supportFragmentManager, tag)
//            rootView.txt_MainFilterDialog_all.setOnClickListener {
//                allDataFiltering()
//                dismiss()
//            }
//            rootView.txt_MainFilterDialog_gain.setOnClickListener {
//                gainDataFiltering()
//
//            }
//            rootView.txt_MainFilterDialog_loss.setOnClickListener {
//                lossDataFiltering()
//                dismiss()
//            }
//        }
//        MainFilterDialog.getInstance(mContext).apply {
//
//        }
    }
    private fun allDataFiltering(){
        allPortfolioList = databaseController.getAllPortfolioDataInfo()
        stockPortfolioListAdapter?.setDataInfoList(allPortfolioList!!)
        stockPortfolioListAdapter?.notifyDataSetChanged()
        txt_MainActivity_Filter.text = getString(R.string.MainFilterDialog_All)
    }

    private fun gainDataFiltering(){
        val gainDataList = databaseController.getGainPortfolioInfo()
        stockPortfolioListAdapter?.setDataInfoList(gainDataList!!)
        stockPortfolioListAdapter?.notifyDataSetChanged()
        txt_MainActivity_Filter.text = getString(R.string.MainFilterDialog_Gain)
    }

    private fun lossDataFiltering(){
        val lossDataList = databaseController.getLossPortfolioInfo()
        stockPortfolioListAdapter?.setDataInfoList(lossDataList!!)
        stockPortfolioListAdapter?.notifyDataSetChanged()
        txt_MainActivity_Filter.text = getString(R.string.MainFilterDialog_Loss)
    }

    private fun addButtonControl(){
        menu?.getItem(menu?.size()!! - 1)?.isVisible = !stockPortfolioListAdapter?.isEditMode()!!
    }


}