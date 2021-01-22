package com.yjpapp.stockportfolio.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.database.data.MyStockInfo
import com.yjpapp.stockportfolio.ui.presenter.MyStockPresenter
import kotlinx.android.synthetic.main.item_my_stock_list.view.*
import java.util.*

/**
 * MyStockListFragment와 연결된 RecyclerView Adapter
 *
 * @author Yun Jae-park
 * @since 2020.08
 */
class MyStockListAdapter(private var allMySockList: ArrayList<MyStockInfo?>,
                         private val myStockPresenter: MyStockPresenter) :
    RecyclerView.Adapter<MyStockListAdapter.ViewHolder>(){
    private var mContext: Context? = null
    private val moneySymbol = Currency.getInstance(Locale.KOREA).symbol
    private var editModeOn = false

    inner class ViewHolder(var view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val inflater = mContext?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view:View = inflater.inflate(R.layout.item_my_stock_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        bindDataList(holder, position)
        bindEditMode(holder)
    }

    override fun getItemCount(): Int {
        return allMySockList.size
    }

    private fun bindDataList(holder: ViewHolder, position: Int){
        holder.itemView.apply {
            //상단 데이터
            txt_subject_name.text = allMySockList[position]?.subjectName
            txt_gain_data.text = allMySockList[position]?.realPainLossesAmount
            txt_gain_percent_data.text = "("+ allMySockList[position]?.gainPercent + ")"
            //왼쪽
            txt_purchase_date_data.text = allMySockList[position]?.purchaseDate
            txt_holding_quantity_data.text = allMySockList[position]?.purchaseCount.toString()
            //오른쪽
            txt_purchase_price_data.text = moneySymbol + allMySockList[position]?.purchasePrice
            txt_current_price_data.text = moneySymbol + allMySockList[position]?.currentPrice

            var realPainLossesAmountNumber = ""
            val realPainLossesAmountSplit = allMySockList[position]?.realPainLossesAmount!!.split(",")
            for (i in realPainLossesAmountSplit.indices) {
                realPainLossesAmountNumber += realPainLossesAmountSplit[i]
            }
            if(realPainLossesAmountNumber.toDouble()>=0){
                txt_gain_data.setTextColor(mContext!!.getColor(R.color.color_e52b4e))
                txt_gain_percent_data.setTextColor(mContext!!.getColor(R.color.color_e52b4e))
            }else{
                txt_gain_data.setTextColor(mContext!!.getColor(R.color.color_4876c7))
                txt_gain_percent_data.setTextColor(mContext!!.getColor(R.color.color_4876c7))
            }

            setOnLongClickListener {
                myStockPresenter.onMyStockListLongClick()
                return@setOnLongClickListener true
            }
            //편집 버튼
            txt_edit.setOnClickListener {
                myStockPresenter.onEditButtonClick(position)
            }
            //매도 버튼
            txt_sell.setOnClickListener {
                myStockPresenter.onSellButtonClick(position)
            }
            //삭제 버튼
            txt_delete.setOnClickListener {
                myStockPresenter.onDeleteButtonClick(position)
            }
        }
    }
    private fun bindEditMode(holder: MyStockListAdapter.ViewHolder){
        holder.itemView.apply {
            if(editModeOn){
                lin_EditMode.visibility = View.VISIBLE
            }else{
                lin_EditMode.visibility = View.GONE
            }
        }
    }

    fun isEditMode(): Boolean{
        return editModeOn
    }
    fun setEditMode(deleteModeOn: Boolean){
        this.editModeOn = deleteModeOn
    }

    fun setMyStockList(allMyStockList: ArrayList<MyStockInfo?>){
        this.allMySockList = allMyStockList
    }
}