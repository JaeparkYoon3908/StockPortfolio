package com.yjpapp.stockportfolio.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.model.DataInfo
import kotlinx.android.synthetic.main.item_main_list2.view.*
import java.util.*
import kotlin.collections.ArrayList

class MainListAdapter(private val data: ArrayList<DataInfo?>?, callback: DBController) :
    RecyclerView.Adapter<MainListAdapter.ViewHolder>(){
    private var mContext: Context? = null
    private var dataInfoList = ArrayList<DataInfo?>()
    private var dbController: DBController
    private var editModeOn: Boolean = false
    private var allCheckClick: Boolean = false
    private val moneySymbol = Currency.getInstance(Locale.KOREA).symbol

    interface DBController{
        fun edit(position: Int)
        fun delete(position: Int)
    }

    init {
        if (data != null) {
            this.dataInfoList = data
        }
        this.dbController = callback
    }

    class ViewHolder(private var view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val inflater = mContext?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view:View = inflater.inflate(R.layout.item_main_list2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // 아이템을 길게 눌렀을 때 편집모드로 전환.
        holder.itemView.setOnLongClickListener {
            editModeOn = !isEditMode() //edit 모드가 꺼져있으면 키고, 켜져 있으면 끈다.
            notifyDataSetChanged()
            return@setOnLongClickListener true
        }
        holder.itemView.txt_edit.setOnClickListener {
            dbController.edit(position)
        }
        holder.itemView.txt_delete.setOnClickListener {
            dbController.delete(position)
        }
//        bindCheckBox(holder, position)
        bindDataList(holder, position)
        bindEditMode(holder, position)

    }

    override fun getItemCount(): Int {
        return dataInfoList.size
    }

    fun deleteList(position: Int){
        dataInfoList.removeAt(position)
    }

    fun getDataInfoList(): ArrayList<DataInfo?>{
        return dataInfoList
    }

    fun setDataInfoList(dataInfoList: ArrayList<DataInfo?>){
        this.dataInfoList = dataInfoList
    }

    fun setEditMode(isEditMode: Boolean){
        this.editModeOn = isEditMode
    }
    fun isEditMode():Boolean{
        return editModeOn
    }

    private fun bindDataList(holder: ViewHolder, position: Int){

        //상단 데이터
        holder.itemView.txt_subject_name.text = dataInfoList[position]?.subjectName
        holder.itemView.txt_gain_data.text = moneySymbol + dataInfoList[position]?.realPainLossesAmount
        //왼쪽
        holder.itemView.txt_purchase_date_data.text = dataInfoList[position]?.purchaseDate
        holder.itemView.txt_sell_date_data.text = dataInfoList[position]?.sellDate
        holder.itemView.txt_gain_percent_data.text = dataInfoList[position]?.gainPercent
        //오른쪽
        holder.itemView.txt_purchase_price_data.text = moneySymbol + dataInfoList[position]?.purchasePrice
        holder.itemView.txt_sell_price_data.text = moneySymbol + dataInfoList[position]?.sellPrice
        holder.itemView.txt_sell_count_data.text = dataInfoList[position]?.sellCount.toString()

        var realPainLossesAmountNumber = ""
        val realPainLossesAmountSplit = dataInfoList[position]?.realPainLossesAmount!!.split(",")
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
        //TODO 수익률 계산 제대로 하기. (마이너스면 파란색, 플러스면 빨간색)

    }
    private fun bindEditMode(holder: ViewHolder, position: Int){
        if(editModeOn){
            holder.itemView.lin_EditMode.visibility = View.VISIBLE
        }else{
            holder.itemView.lin_EditMode.visibility = View.GONE
        }
    }
}