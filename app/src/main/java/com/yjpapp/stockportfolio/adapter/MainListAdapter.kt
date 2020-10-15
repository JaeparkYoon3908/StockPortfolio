package com.yjpapp.stockportfolio.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.model.DataInfo
import com.yjpapp.stockportfolio.util.Utils
import kotlinx.android.synthetic.main.item_main_list.view.*

class MainListAdapter(private val data: ArrayList<DataInfo>, callback: OnDeleteMode) :
    RecyclerView.Adapter<MainListAdapter.ViewHolder>(){
    private var mContext: Context? = null
    private var dataInfoList = ArrayList<DataInfo>()
    private var onDeleteMode: OnDeleteMode
    private var deleteModeOn: Boolean = false
    private var allCheckClick: Boolean = false

    interface OnDeleteMode{
        fun deleteModeOn()
    }

    init {
        this.dataInfoList = data
        this.onDeleteMode = callback
    }

    class ViewHolder(private var view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val inflater = mContext?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view:View = inflater.inflate(R.layout.item_main_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // 아이템을 길게 눌렀을 때 삭제모드로 전환.
        holder.itemView.setOnLongClickListener {
            deleteModeOn = true
            onDeleteMode.deleteModeOn()
            notifyDataSetChanged()
            return@setOnLongClickListener true
        }
        if(deleteModeOn){
            holder.itemView.rel_checkbox.visibility = View.VISIBLE
        }else{
            holder.itemView.rel_checkbox.visibility = View.GONE
        }
        holder.itemView.checkbox!!.isChecked = allCheckClick
        holder.itemView.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            dataInfoList[position].isDeleteCheck = isChecked
        }
        
        if(holder.itemView.checkbox.isChecked){
            Utils.logcat("position : $position isChecked!!")
        }else{
            Utils.logcat("position : $position notChecked!!")
        }
    }

    override fun getItemCount(): Int {
        return dataInfoList.size
    }

    fun deleteList(position: Int){
        dataInfoList.removeAt(position)
    }

    fun setDeleteModeOff(){
        deleteModeOn = false
    }

    fun setAllCheckClicked(check: Boolean){
        allCheckClick = check
    }

    fun getDataInfoList(): ArrayList<DataInfo>{
        return dataInfoList
    }
}