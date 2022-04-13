package com.yjpapp.stockportfolio.function.incomenote

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.common.widget.CommonLoadingView
import com.yjpapp.stockportfolio.databinding.ItemIncomeNoteListBinding
import com.yjpapp.stockportfolio.databinding.ItemLoadingBinding
import com.yjpapp.stockportfolio.model.response.RespIncomeNoteListInfo
import com.yjpapp.swipelayout.SwipeLayout
import com.yjpapp.swipelayout.adapters.RecyclerSwipeAdapter
import com.yjpapp.swipelayout.implments.SwipeItemRecyclerMangerImpl
import java.util.*

/**
 * IncomeNotePresenter와 연결된 RecyclerView Adapter
 *
 * @author Yoon Jae-park
 * @since 2020.08
 */
class IncomeNoteListAdapter(
    var incomeNoteListInfo: ArrayList<RespIncomeNoteListInfo.IncomeNoteInfo>,
    var callBack: CallBack?
) : RecyclerSwipeAdapter<RecyclerView.ViewHolder>()
{
    private val VIEW_TYPE_LIST = 0
    private val VIEW_TYPE_LOADING = 1

    private lateinit var mContext: Context
    private val swipeItemManger = SwipeItemRecyclerMangerImpl(this)
    var isLoadingAnimationStart = false

    inner class IncomeNoteListViewHolder(var binding: ItemIncomeNoteListBinding) : RecyclerView.ViewHolder(binding.root) {}
    inner class LoadingViewHolder(var binding: ItemLoadingBinding) : RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mContext = parent.context
        when (viewType) {
            VIEW_TYPE_LOADING -> {
                val binding: ItemLoadingBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_loading,
                    parent,
                    false
                )
                return LoadingViewHolder(binding)
            }
            else -> {
                val binding: ItemIncomeNoteListBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_income_note_list,
                    parent,
                    false
                )
                return IncomeNoteListViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LoadingViewHolder -> {
                holder.apply {
                    binding.ivLoading.setLoadingImageColor(CommonLoadingView.LoadingColorType.BLACK)
                    if (isLoadingAnimationStart) {
                        binding.ivLoading.startAnimation()
                        binding.ivLoading.visibility = View.VISIBLE
                    } else {
                        binding.ivLoading.stopAnimation()
                        binding.ivLoading.visibility = View.GONE
                    }
                }
            }
            is IncomeNoteListViewHolder -> {
                holder.apply {
                    bindDataList(holder, position)
                    bindSwipeLayout(holder, position)
                }
            }
        }
    }

    private fun bindDataList(holder: IncomeNoteListViewHolder, position: Int) {
        holder.binding.apply {
            incomeNote = incomeNoteListInfo[position]
        }
    }

    private fun bindSwipeLayout(holder: IncomeNoteListViewHolder, position: Int) {
        swipeItemManger.bindView(holder.itemView, position)
        holder.binding.apply {
            swipeLayoutIncomeNote.addSwipeListener(object : SwipeLayout.SwipeListener {
                override fun onStartOpen(layout: SwipeLayout) {
                    swipeItemManger.closeAllExcept(layout)
                }
                override fun onOpen(layout: SwipeLayout) {}
                override fun onStartClose(layout: SwipeLayout) {}
                override fun onClose(layout: SwipeLayout) {}
                override fun onUpdate(layout: SwipeLayout, leftOffset: Int, topOffset: Int) {}
                override fun onHandRelease(layout: SwipeLayout, xvel: Float, yvel: Float) {}
            })
            txtEdit.setOnClickListener {
                callBack?.onEditButtonClick(incomeNoteListInfo[position])
            }
            txtDelete.setOnClickListener {
                callBack?.onDeleteButtonClick(incomeNoteListInfo[position], position)
            }
        }
    }

    override fun getSwipeLayoutResourceId(position: Int): Int {
        return R.id.swipeLayout_incomeNote
    }

    fun closeSwipeLayout() {
        swipeItemManger.closeAllItems()
    }

    override fun getItemId(position: Int): Long {
        return incomeNoteListInfo[position].hashCode().toLong()
    }

    interface CallBack {
        fun onEditButtonClick(respIncomeNoteListInfo: RespIncomeNoteListInfo.IncomeNoteInfo?)
        fun onDeleteButtonClick(respIncomeNoteListInfo: RespIncomeNoteListInfo.IncomeNoteInfo?, position: Int)
    }

    override fun getItemCount(): Int {
        return incomeNoteListInfo.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) {
            VIEW_TYPE_LOADING
        } else {
            VIEW_TYPE_LIST
        }
    }
}