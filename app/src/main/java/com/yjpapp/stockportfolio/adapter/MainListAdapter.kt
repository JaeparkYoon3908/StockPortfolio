package com.yjpapp.stockportfolio.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.activity.MainActivity
import com.yjpapp.stockportfolio.model.DataInfo
import com.yjpapp.stockportfolio.util.Utils
import kotlinx.android.synthetic.main.item_main_list.view.*

class MainListAdapter(private val data: ArrayList<DataInfo>, callback: OnDeleteMode) :
    RecyclerView.Adapter<MainListAdapter.ViewHolder>(){
    private var mContext: Context? = null
    private var dataList = ArrayList<DataInfo>()
    private var onDeleteMode: OnDeleteMode
    private var hideCheckBox: Boolean = true

    interface OnDeleteMode{
        fun deleteModeOn()
    }

    init {
        this.dataList = data
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
            hideCheckBox = false
            onDeleteMode.deleteModeOn()
            notifyDataSetChanged()
            return@setOnLongClickListener true
        }
        if(hideCheckBox){
            holder.itemView.checkbox.visibility = View.GONE
        }else{
            holder.itemView.checkbox.visibility = View.VISIBLE
        }
        holder.itemView.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->  }
        
        if(holder.itemView.checkbox.isChecked){
            Utils.logcat("position : " + position + "isChecked!!")
        }else{
            Utils.logcat("position : " + position + "notChecked!!")
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun deleteList(position: Int){
        dataList.removeAt(position)
    }

    fun hideCheckBox(){
        hideCheckBox = true
    }
}