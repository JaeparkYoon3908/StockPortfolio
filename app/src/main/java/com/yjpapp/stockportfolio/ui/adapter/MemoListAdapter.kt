package com.yjpapp.stockportfolio.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.database.data.MemoInfo
import com.yjpapp.stockportfolio.ui.presenter.MemoListPresenter
import kotlinx.android.synthetic.main.item_memo_list.view.*

/**
 * MemoListPresenter와 연결된 RecyclerView Adapter
 *
 * @author Yun Jae-park
 * @since 2020.08
 */
class MemoListAdapter(private var memoListData: ArrayList<MemoInfo?>, private val memoListPresenter: MemoListPresenter) :
    RecyclerView.Adapter<MemoListAdapter.ViewHolder>(){
    private var deleteModeOn = false
    private lateinit var mContext: Context

    inner class ViewHolder(var view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view:View = inflater.inflate(R.layout.item_memo_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            view.txt_MemoList_Date.text = memoListData[position]?.date
            view.txt_MemoList_Title.text = memoListData[position]?.title
            view.txt_MemoList_NoteCount.text = memoListData[position]?.content
            view.img_MemoList_Check.isSelected = memoListData[position]?.deleteChecked!! == "true"

            view.setOnLongClickListener {
                memoListPresenter.onMemoListLongClicked(position)
                return@setOnLongClickListener true
            }

            if(deleteModeOn){
                view.img_MemoList_Check.visibility = View.VISIBLE
                view.setOnClickListener {
                    view.img_MemoList_Check.isSelected = !view.img_MemoList_Check.isSelected
                    if(view.img_MemoList_Check.isSelected){
                        memoListPresenter.clickMemoDeleteCheck(position, true)

                    }else{
                        memoListPresenter.clickMemoDeleteCheck(position, false)
                    }
                }
            }else{
                view.img_MemoList_Check.visibility = View.GONE
                view.setOnClickListener {
                    memoListPresenter.onMemoListClicked(position)
                }
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