package com.yjpapp.stockportfolio.ui.memo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.database.sqlte.data.MemoInfo

/**
 * MemoListPresenter와 연결된 RecyclerView Adapter
 *
 * @author Yoon Jae-park
 * @since 2020.08
 */
class MemoListAdapter(
    private var memoListData: ArrayList<MemoInfo?>,
    private val memoListPresenter: MemoListPresenter
) :
    RecyclerView.Adapter<MemoListAdapter.ViewHolder>() {
    private var deleteModeOn = false

    inner class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        val txt_MemoList_Date: TextView = view.findViewById(R.id.txt_MemoList_Date)
        val txt_MemoList_Title: TextView = view.findViewById(R.id.txt_MemoList_Title)
        val txt_MemoList_NoteCount: TextView = view.findViewById(R.id.txt_MemoList_NoteCount)
        val img_MemoList_Check: ImageView = view.findViewById(R.id.img_MemoList_Check)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.item_memo_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            txt_MemoList_Date.text = memoListData[position]?.date
            txt_MemoList_Title.text = memoListData[position]?.title
            txt_MemoList_NoteCount.text = memoListData[position]?.content
            img_MemoList_Check.isSelected = memoListData[position]?.deleteChecked!! == "true"

            itemView.setOnLongClickListener {
                memoListPresenter.onMemoListLongClicked(position)
                return@setOnLongClickListener true
            }

            if (deleteModeOn) {
                img_MemoList_Check.visibility = View.VISIBLE
                itemView.setOnClickListener {
                    img_MemoList_Check.isSelected = !img_MemoList_Check.isSelected
                    if (img_MemoList_Check.isSelected) {
                        memoListPresenter.clickMemoDeleteCheck(position, true)

                    } else {
                        memoListPresenter.clickMemoDeleteCheck(position, false)
                    }
                }
            } else {
                img_MemoList_Check.visibility = View.GONE
                itemView.setOnClickListener {
                    memoListPresenter.onMemoListClicked(position)
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return memoListData.size
    }

    fun setMemoListData(memoListData: ArrayList<MemoInfo?>) {
        this.memoListData = memoListData
    }

    fun getMemoListData(): ArrayList<MemoInfo?> {
        return memoListData
    }

    fun isDeleteModeOn(): Boolean {
        return deleteModeOn
    }

    fun setDeleteModeOn(deleteModeOn: Boolean) {
        this.deleteModeOn = deleteModeOn
    }

}