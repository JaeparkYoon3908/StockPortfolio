package com.yjpapp.stockportfolio.ui.income_note

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.database.Databases
import com.yjpapp.stockportfolio.database.model.PortfolioInfo
import com.yjpapp.stockportfolio.ui.dialog.EditPortfolioDialog
import com.yjpapp.stockportfolio.ui.dialog.MainFilterDialog
import com.yjpapp.stockportfolio.ui.memo.MemoListActivity
import com.yjpapp.stockportfolio.util.Utils
import jp.wasabeef.recyclerview.animators.FadeInAnimator
import kotlinx.android.synthetic.main.dialog_add_portfolio.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class IncomeNoteFragment: Fragment(),
    IncomeNoteListAdapter.MainActivityCallBack, MainFilterDialog.MainFilterClicked {
    private lateinit var mContext: Context
    private lateinit var rootView: View

    //layout 변수
    private lateinit var txt_total_realization_gains_losses_data: TextView
    private lateinit var txt_total_realization_gains_losses_percent: TextView
    private lateinit var lin_MainActivity_Filter: LinearLayout
    private lateinit var txt_MainActivity_Filter: TextView
    private lateinit var txt_MainActivity_Edit: TextView
    private lateinit var recyclerview_MainActivity: RecyclerView

    private var incomeNoteListAdapter: IncomeNoteListAdapter? = null
    private var allPortfolioList: ArrayList<PortfolioInfo?>? = null
    private var insertMode: Boolean = false
    private var editSelectPosition = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_income_note, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
        initLayout()

    }

    override fun onResume() {
        super.onResume()
        activity?.invalidateOptionsMenu()
    }

    private fun addClicked() {
        EditPortfolioDialog(mContext).apply {
            if (!isShowing) {
                show()
                txt_complete.setOnClickListener {
                    runDialogCompleteClick(this)
                }
                txt_cancel.setOnClickListener {
                    dismiss()
                }
            }
        }
//        val symbol = "005930.KS"
//        val region = "KR"
//        YahooFinanceProtocolManager.getInstance(mContext).getStockProfile(symbol, region,
//            object: Callback<JsonObject?> {
//            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
//                if(response.code() == 200 || response.code() == 204){
//                    try {
////                    val formattedResult = StringBuilder()
//                        val jsonObject = JSONObject(response.body().toString())
//                        val priceJSONArray = jsonObject.getJSONObject("price").toString()
//                        val price: Price = Gson().fromJson(priceJSONArray, Price::class.java)
//                        Log.d("YJP", "price = $price")
//                    } catch (e: JSONException) {
//                        e.printStackTrace()
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
//                Log.d("RequestResult", "RetrofitExample, Type : get, Result : onFailure, Error Message : " + t.message)
//            }
//        })
    }

    override fun onEditClicked(position: Int) {
        insertMode = false
        incomeNoteListAdapter?.setEditMode(false)
        incomeNoteListAdapter?.notifyDataSetChanged()
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

    override fun onDeleteClicked(position: Int) {
        var dataList = incomeNoteListAdapter?.getDataInfoList()!!
        val id: Int = dataList[position]?.id!!
        (activity as MainActivity).databaseController.deleteData(id, Databases.TABLE_INCOME_NOTE)

        dataList = (activity as MainActivity).databaseController.getAllIncomeNoteList()
        incomeNoteListAdapter?.setDataInfoList(dataList)
//        incomeNoteListAdapter?.setEditMode(false)
        incomeNoteListAdapter?.notifyItemRemoved(position)
        incomeNoteListAdapter?.notifyItemRangeRemoved(position,incomeNoteListAdapter?.itemCount!!)
        addButtonControl()
        bindTotalGainData()
    }

    override fun onItemLongClicked() {
        addButtonControl()
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

    private var menu: Menu? = null
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_income_note, menu)
        this.menu = menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.menu_IncomeNoteActivity_Add -> {
                insertMode = true
                addClicked()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initData(){
        allPortfolioList  = (activity as MainActivity).databaseController.getAllIncomeNoteList()
    }


    private fun initLayout(){
        setHasOptionsMenu(true)

        txt_total_realization_gains_losses_data = rootView.findViewById(R.id.txt_total_realization_gains_losses_data)
        txt_total_realization_gains_losses_percent = rootView.findViewById(R.id.txt_total_realization_gains_losses_percent)
        lin_MainActivity_Filter = rootView.findViewById(R.id.lin_MainActivity_Filter)
        txt_MainActivity_Filter = rootView.findViewById(R.id.txt_MainActivity_Filter)
        txt_MainActivity_Edit = rootView.findViewById(R.id.txt_MainActivity_Edit)
        recyclerview_MainActivity = rootView.findViewById(R.id.recyclerview_MainActivity)

        lin_MainActivity_Filter.setOnClickListener(onClickListener)
        txt_MainActivity_Edit.setOnClickListener(onClickListener)

        txt_total_realization_gains_losses_data.isSelected = true
        txt_total_realization_gains_losses_percent.isSelected = true

        bindTotalGainData()
        initRecyclerView()
    }

    private val onClickListener = View.OnClickListener {view: View? ->
        when(view?.id){
            R.id.lin_MainActivity_BottomMenu_Memo -> {
                val intent = Intent(mContext, MemoListActivity::class.java)
                startActivity(intent)
            }

            R.id.txt_MainActivity_Edit -> {
                activity?.window?.attributes?.windowAnimations = R.style.AnimationPopupStyle
                if (allPortfolioList?.size!! > 0) {
                    incomeNoteListAdapter?.setEditMode(!incomeNoteListAdapter?.isEditMode()!!)
                    incomeNoteListAdapter?.notifyDataSetChanged()
                    addButtonControl()
                }

            }
            R.id.lin_MainActivity_Filter -> {
                val mainFilterDialog = MainFilterDialog(this)
                mainFilterDialog.show(childFragmentManager, tag)
            }
        }

    }

    private fun bindTotalGainData(){
        allPortfolioList  = (activity as MainActivity).databaseController.getAllIncomeNoteList()
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
            txt_total_realization_gains_losses_data.setTextColor(mContext.getColor(R.color.color_e52b4e))
            txt_total_realization_gains_losses_percent.setTextColor(mContext.getColor(R.color.color_e52b4e))
        }else{
            txt_total_realization_gains_losses_data.setTextColor(mContext.getColor(R.color.color_4876c7))
            txt_total_realization_gains_losses_percent.setTextColor(mContext.getColor(R.color.color_4876c7))
        }
        txt_total_realization_gains_losses_percent.text = Utils.getRoundsPercentNumber(totalGainPercent)
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

        incomeNoteListAdapter = IncomeNoteListAdapter(allPortfolioList, this)
        recyclerview_MainActivity.adapter = incomeNoteListAdapter
        recyclerview_MainActivity.itemAnimator = FadeInAnimator()

    }

    private fun runDialogCancelClick(editPortfolioDialog: EditPortfolioDialog){
        editPortfolioDialog.run {
            addButtonControl()
            dismiss()
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
                (activity as MainActivity).databaseController.insertIncomeNoteData(dataInfo)
            }else{
                dataInfo.id = allPortfolioList!![editSelectPosition]!!.id
                (activity as MainActivity).databaseController.updateIncomeNoteData(dataInfo)
            }
            dismiss()
        }
        val newDataInfo = (activity as MainActivity).databaseController.getAllIncomeNoteList()
        incomeNoteListAdapter?.setDataInfoList(newDataInfo)
        if(insertMode){
            incomeNoteListAdapter?.notifyItemInserted(incomeNoteListAdapter?.itemCount!! - 1)
        }else{
            incomeNoteListAdapter?.notifyDataSetChanged()
        }
        recyclerview_MainActivity.scrollToPosition(newDataInfo.size - 1)
        bindTotalGainData()
    }

    private fun allDataFiltering(){
        allPortfolioList = (activity as MainActivity).databaseController.getAllIncomeNoteList()
        incomeNoteListAdapter?.setDataInfoList(allPortfolioList!!)
        incomeNoteListAdapter?.notifyDataSetChanged()
        txt_MainActivity_Filter.text = getString(R.string.MainFilterDialog_All)
    }

    private fun gainDataFiltering(){
        val gainDataList = (activity as MainActivity).databaseController.getGainIncomeNoteList()
        incomeNoteListAdapter?.setDataInfoList(gainDataList!!)
        incomeNoteListAdapter?.notifyDataSetChanged()
        txt_MainActivity_Filter.text = getString(R.string.MainFilterDialog_Gain)
    }

    private fun lossDataFiltering(){
        val lossDataList = (activity as MainActivity).databaseController.getLossIncomeNoteList()
        incomeNoteListAdapter?.setDataInfoList(lossDataList!!)
        incomeNoteListAdapter?.notifyDataSetChanged()
        txt_MainActivity_Filter.text = getString(R.string.MainFilterDialog_Loss)
    }

    private fun addButtonControl(){
        menu?.getItem(menu?.size()!! - 1)?.isVisible = !incomeNoteListAdapter?.isEditMode()!!
    }
}