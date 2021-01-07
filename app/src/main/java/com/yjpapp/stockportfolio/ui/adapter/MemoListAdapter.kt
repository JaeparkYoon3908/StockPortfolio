package com.yjpapp.stockportfolio.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.database.data.MemoInfo
import com.yjpapp.stockportfolio.util.Utils
import kotlinx.android.synthetic.main.item_memo_list.view.*

class MemoListAdapter(private var memoListData: ArrayList<MemoInfo?>, private val memoActivityCallBack: MemoActivityCallBack) :
    RecyclerView.Adapter<MemoListAdapter.ViewHolder>(){
    private var deleteModeOn = false

    interface MemoActivityCallBack{
        fun onMemoListLongClicked()
        fun onMemoListClicked(position: Int)
    }
    private lateinit var mContext: Context

    inner class ViewHolder(var view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view:View = inflater.inflate(R.layout.item_memo_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.txt_MemoList_Date.text = memoListData[position]?.date
        holder.view.txt_MemoList_Title.text = memoListData[position]?.title
        holder.view.txt_MemoList_NoteCount.text = memoListData[position]?.content
        holder.view.img_MemoList_Check.isSelected = false

        if(memoListData[position]?.deleteChecked!! == "true"){
            holder.view.img_MemoList_Check.isSelected = true
        }

        holder.view.setOnLongClickListener {
            Utils.runVibration(mContext, 100)
            deleteModeOn = !deleteModeOn
            memoListData[position]?.deleteChecked = "true"
            notifyDataSetChanged()
            memoActivityCallBack.onMemoListLongClicked()
            return@setOnLongClickListener true
        }

        if(deleteModeOn){
            holder.view.img_MemoList_Check.visibility = View.VISIBLE
            holder.view.setOnClickListener {
                holder.view.img_MemoList_Check.isSelected = !holder.view.img_MemoList_Check.isSelected
                if(holder.view.img_MemoList_Check.isSelected){
                    memoListData[position]?.deleteChecked = "true"
                }else{
                    memoListData[position]?.deleteChecked = "false"
                }
//                memoListData[position]?.deleteChecked = holder.view.img_MemoList_Check.isSelected
            }
        }else{
            holder.view.img_MemoList_Check.visibility = View.GONE
            holder.view.setOnClickListener {
                memoActivityCallBack.onMemoListClicked(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return memoListData.size
    }

    fun setMemoListData(memoListData: ArrayList<MemoInfo?>){
        this.memoListData = memoListData
    }

    fun getMemoListData(): ArrayList<MemoInfo?>{
        return memoListData
    }

    fun isDeleteModeOn(): Boolean{
        return deleteModeOn
    }
    fun setDeleteModeOn(deleteModeOn: Boolean){
        this.deleteModeOn = deleteModeOn
    }

}