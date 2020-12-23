package com.yjpapp.stockportfolio.ui.income_note

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.database.model.PortfolioInfo
import com.yjpapp.stockportfolio.util.Utils
import kotlinx.android.synthetic.main.item_income_note_list.view.*
import java.util.*
import kotlin.collections.ArrayList


class IncomeNoteListAdapter(val data: ArrayList<PortfolioInfo?>?, callback: MainActivityCallBack) :
    RecyclerView.Adapter<IncomeNoteListAdapter.ViewHolder>(){
    private lateinit var mContext: Context
    private var dataInfoList = ArrayList<PortfolioInfo?>()
    private var mainActivityCallBack: MainActivityCallBack
    private var editModeOn: Boolean = false
    private var allCheckClick: Boolean = false
    private val moneySymbol = Currency.getInstance(Locale.KOREA).symbol

    interface MainActivityCallBack{
        fun onEditClicked(position: Int)
        fun onDeleteClicked(position: Int)
        fun onItemLongClicked()
    }

    init {
        if (data != null) {
            this.dataInfoList = data
        }
        this.mainActivityCallBack = callback
    }

    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view:View = inflater.inflate(R.layout.item_income_note_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // 아이템을 길게 눌렀을 때 편집모드로 전환.
        holder.apply {
            itemView.setOnLongClickListener {
                Utils.runVibration(mContext, 100)
                editModeOn = !isEditMode() //edit 모드가 꺼져있으면 키고, 켜져 있으면 끈다.
                notifyDataSetChanged()
                mainActivityCallBack.onItemLongClicked()
                return@setOnLongClickListener true
            }
            itemView.txt_edit.setOnClickListener {
                mainActivityCallBack.onEditClicked(position)
            }
            itemView.txt_delete.setOnClickListener {
                mainActivityCallBack.onDeleteClicked(position)
            }
            itemView.txt_gain_data.isSelected = true

            bindDataList(holder, position)
            bindEditMode(holder)
        }


    }

    override fun getItemCount(): Int {
        return dataInfoList.size
    }

    fun deleteList(position: Int){
        dataInfoList.removeAt(position)
    }

    fun getDataInfoList(): ArrayList<PortfolioInfo?>{
        return dataInfoList
    }

    fun setDataInfoList(portfolioInfoList: ArrayList<PortfolioInfo?>){
        this.dataInfoList = portfolioInfoList
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
            holder.itemView.txt_gain_data.setTextColor(mContext.getColor(R.color.color_e52b4e))
            holder.itemView.txt_gain_percent_data.setTextColor(mContext.getColor(R.color.color_e52b4e))
        }else{
            holder.itemView.txt_gain_data.setTextColor(mContext.getColor(R.color.color_4876c7))
            holder.itemView.txt_gain_percent_data.setTextColor(mContext.getColor(R.color.color_4876c7))
        }
    }
    private fun bindEditMode(holder: ViewHolder){
        if(editModeOn){
            holder.itemView.lin_EditMode.visibility = View.VISIBLE
        }else{
            holder.itemView.lin_EditMode.visibility = View.GONE
        }
    }
}