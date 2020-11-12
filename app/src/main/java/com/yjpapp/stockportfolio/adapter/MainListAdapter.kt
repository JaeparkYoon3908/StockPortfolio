package com.yjpapp.stockportfolio.adapter

import android.content.Context
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.model.DataInfo
import kotlinx.android.synthetic.main.item_main_list2.view.*

class MainListAdapter(private val data: ArrayList<DataInfo?>?, callback: OnDeleteMode) :
    RecyclerView.Adapter<MainListAdapter.ViewHolder>(){
    private var mContext: Context? = null
    private var dataInfoList = ArrayList<DataInfo?>()
    private var onEditMode: OnDeleteMode
    private var editModeOn: Boolean = false
    private var allCheckClick: Boolean = false

    interface OnDeleteMode{
        fun editModeOn()
    }

    init {
        if (data != null) {
            this.dataInfoList = data
        }
        this.onEditMode = callback
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
            if(!editModeOn){
                editModeOn = true
                onEditMode.editModeOn()
                notifyDataSetChanged()
            }
            return@setOnLongClickListener true
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

//    fun setAllCheckClicked(check: Boolean){
//        allCheckClick = check
//    }

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

//    private fun bindCheckBox(holder: ViewHolder, position: Int){
//        if(deleteModeOn){
//            holder.itemView.rel_checkbox.visibility = View.VISIBLE
//        }else{
//            holder.itemView.rel_checkbox.visibility = View.GONE
//        }
//        holder.itemView.checkbox!!.isChecked = allCheckClick
//        holder.itemView.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
//            if(isChecked){
//                dataInfoList[position]?.isDeleteCheck = mContext?.getString(R.string.common_true)
//            }else{
//                dataInfoList[position]?.isDeleteCheck = mContext?.getString(R.string.common_false)
//            }
//        }
//
//        if(holder.itemView.checkbox.isChecked){
//            Utils.logcat("position : $position isChecked!!")
//        }else{
//            Utils.logcat("position : $position notChecked!!")
//        }
//    }
    private fun bindDataList(holder: ViewHolder, position: Int){
//        holder.itemView.list_1.text = dataInfoList[position]?.sellDate + "\n" + dataInfoList[position]?.subjectName
//        holder.itemView.list_2.text = dataInfoList[position]?.realPainLossesAmount + "\n" + dataInfoList[position]?.gainPercent
//        holder.itemView.list_3.text = dataInfoList[position]?.purchasePrice + "\n" + dataInfoList[position]?.sellPrice

        //상단 데이터
        holder.itemView.txt_subject_name.text = dataInfoList[position]?.subjectName
        holder.itemView.txt_gain_data.text = dataInfoList[position]?.realPainLossesAmount
        //왼쪽
        holder.itemView.txt_purchase_date_data.text = dataInfoList[position]?.purchaseDate
        holder.itemView.txt_sell_date_data.text = dataInfoList[position]?.sellDate
        holder.itemView.txt_gain_percent_data.text = dataInfoList[position]?.gainPercent
        //오른쪽
        holder.itemView.txt_purchase_price_data.text = dataInfoList[position]?.purchasePrice
        holder.itemView.txt_sell_price_data.text = dataInfoList[position]?.sellPrice
        holder.itemView.txt_sell_count_data.text = dataInfoList[position]?.sellCount.toString()
    }
    private fun bindEditMode(holder: ViewHolder, position: Int){
        if(editModeOn){
            holder.itemView.lin_EditMode.visibility = View.VISIBLE
        }else{
            holder.itemView.lin_EditMode.visibility = View.GONE
        }

    }
}