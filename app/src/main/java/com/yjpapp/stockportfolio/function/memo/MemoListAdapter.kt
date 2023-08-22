package com.yjpapp.stockportfolio.function.memo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yjpapp.data.localdb.room.memo.MemoListEntity
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.databinding.ItemMemoListBinding

/**
 * MemoListPresenter와 연결된 RecyclerView Adapter
 *
 * @author Yoon Jae-park
 * @since 2020.08
 */
class MemoListAdapter(
    var memoListData: MutableList<MemoListEntity>,
    var callBack: CallBack?
) :
    RecyclerView.Adapter<MemoListAdapter.ViewHolder>() {
    var isDeleteModeOn = false

    inner class ViewHolder(val binding: ItemMemoListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemMemoListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_memo_list,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            memoData = memoListData[position]
            deleteModeOn = this@MemoListAdapter.isDeleteModeOn
        }
        holder.itemView.setOnLongClickListener {
            callBack?.onMemoListLongClicked(position = position)

            return@setOnLongClickListener true
        }
        holder.itemView.setOnClickListener {
            if (isDeleteModeOn) {
                holder.binding.imgMemoListCheck.isSelected = !holder.binding.imgMemoListCheck.isSelected
                val imgMemoListCheck = holder.binding.imgMemoListCheck.isSelected
                callBack?.onMemoListClicked(position = position, imgMemoListCheck)
            } else {
                callBack?.onMemoListClicked(position = position)
            }
        }
    }

    override fun getItemCount(): Int {
        return memoListData.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    interface CallBack {
        fun onMemoListClicked(position: Int, imgMemoListCheck: Boolean)
        fun onMemoListClicked(position: Int)
        fun onMemoListLongClicked(position: Int)
        fun onMemoDeleteCheckClicked(position: Int, deleteCheck: Boolean)
    }
}