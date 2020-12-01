package com.yjpapp.stockportfolio.ui.memo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.model.MemoInfo
import kotlinx.android.synthetic.main.item_memo_list.view.*

class MemoListAdapter(private var memoListData: ArrayList<MemoInfo?>) :
    RecyclerView.Adapter<MemoListAdapter.ViewHolder>(){
    private var mContext: Context? = null

    inner class ViewHolder(var view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val inflater = mContext?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view:View = inflater.inflate(R.layout.item_memo_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.txt_MemoList_Date.text = memoListData[position]?.date
        holder.view.txt_MemoList_Title.text = memoListData[position]?.title
        holder.view.txt_MemoList_NoteCount.text = memoListData[position]?.content
    }

    override fun getItemCount(): Int {
        return memoListData.size
    }

    public fun setMemoListData(memoListData: ArrayList<MemoInfo?>){
        this.memoListData = memoListData
    }

    public fun getMemoListData(): ArrayList<MemoInfo?>{
        return memoListData
    }
}