package com.yjpapp.stockportfolio.ui.my_stock

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.database.model.MyStockInfo
import com.yjpapp.stockportfolio.ui.income_note.IncomeNoteListAdapter
import kotlinx.android.synthetic.main.item_my_stock_list.view.*
import java.util.*

class MyStockListAdapter (private val allMySockList: ArrayList<MyStockInfo>) :
    RecyclerView.Adapter<MyStockListAdapter.ViewHolder>(){
    private var mContext: Context? = null
    private val moneySymbol = Currency.getInstance(Locale.KOREA).symbol

    inner class ViewHolder(var view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val inflater = mContext?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view:View = inflater.inflate(R.layout.item_my_stock_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        bindDataList(holder, position)
    }

    override fun getItemCount(): Int {
        return allMySockList.size
    }

    private fun bindDataList(holder: MyStockListAdapter.ViewHolder, position: Int){

        //상단 데이터
        holder.itemView.txt_subject_name.text = allMySockList[position].subjectName
        holder.itemView.txt_gain_data.text = allMySockList[position].realPainLossesAmount
        holder.itemView.txt_gain_percent_data.text = "("+ allMySockList[position].gainPercent + ")"
        //왼쪽
        holder.itemView.txt_purchase_date_data.text = allMySockList[position].purchaseDate
        holder.itemView.txt_holding_quantity_data.text = allMySockList[position].holdingQuantity
        //오른쪽
        holder.itemView.txt_purchase_price_data.text = moneySymbol + allMySockList[position].purchasePrice
        holder.itemView.txt_current_price_data.text = moneySymbol + allMySockList[position].currentPrice

        var realPainLossesAmountNumber = ""
        val realPainLossesAmountSplit = allMySockList[position].realPainLossesAmount!!.split(",")
        for (i in realPainLossesAmountSplit.indices) {
            realPainLossesAmountNumber += realPainLossesAmountSplit[i]
        }
        if(realPainLossesAmountNumber.toDouble()>=0){
            holder.itemView.txt_gain_data.setTextColor(mContext!!.getColor(R.color.color_e52b4e))
            holder.itemView.txt_gain_percent_data.setTextColor(mContext!!.getColor(R.color.color_e52b4e))
        }else{
            holder.itemView.txt_gain_data.setTextColor(mContext!!.getColor(R.color.color_4876c7))
            holder.itemView.txt_gain_percent_data.setTextColor(mContext!!.getColor(R.color.color_4876c7))
        }
    }
    private fun bindEditMode(holder: IncomeNoteListAdapter.ViewHolder){

    }
}