package com.yjpapp.stockportfolio.ui.memo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.model.MemoInfo
import kotlinx.android.synthetic.main.item_memo_list.view.*

class MemoListAdapter(private var memoListData: ArrayList<MemoInfo?>, private val memoActivityCallBack: MemoListAdapter.MemoActivityCallBack) :
    RecyclerView.Adapter<MemoListAdapter.ViewHolder>(){
    private var deleteModeOn = false

    interface MemoActivityCallBack{
//        fun onEditClicked(position: Int)
//        fun onDeleteClicked(position: Int)
        fun onItemLongClicked(deleteModeOn: Boolean)
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

        holder.view.setOnLongClickListener {
            deleteModeOn = !deleteModeOn
            notifyDataSetChanged()
            memoActivityCallBack.onItemLongClicked(deleteModeOn)
            return@setOnLongClickListener true
        }

        if(deleteModeOn){
            holder.view.img_MemoList_Check.visibility = View.VISIBLE
            holder.view.setOnClickListener {
                holder.view.img_MemoList_Check.isSelected = !holder.view.img_MemoList_Check.isSelected
                memoListData[position]?.deleteChecked = holder.view.img_MemoList_Check.isSelected
            }
        }else{
            holder.view.img_MemoList_Check.visibility = View.GONE
            holder.view.setOnClickListener {
                val intent = Intent(mContext, MemoReadWriteActivity::class.java)
                intent.putExtra(MemoListActivity.INTENT_KEY_LIST_POSITION, position)
                intent.putExtra(MemoListActivity.INTENT_KEY_MEMO_INFO_ID, memoListData[position]?.id)
                intent.putExtra(MemoListActivity.INTENT_KEY_MEMO_INFO_TITLE, memoListData[position]?.title)
                intent.putExtra(MemoListActivity.INTENT_KEY_MEMO_INFO_CONTENT, memoListData[position]?.content)
                intent.putExtra(MemoListActivity.INTENT_KEY_MEMO_MODE, MemoListActivity.MEMO_READ_MODE)

                (mContext as Activity).startActivityForResult(intent, MemoListActivity.REQUEST_READ)
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

    fun setDeleteModeOn(deleteModeOn: Boolean){
        this.deleteModeOn = deleteModeOn
    }

}