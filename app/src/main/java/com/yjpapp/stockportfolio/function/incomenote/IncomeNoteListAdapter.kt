package com.yjpapp.stockportfolio.function.incomenote

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.model.response.RespIncomeNoteInfo
import com.yjpapp.swipelayout.SwipeLayout
import com.yjpapp.swipelayout.adapters.PagingSwipeAdapter
import com.yjpapp.swipelayout.implments.SwipeItemRecyclerMangerImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.SQLException
import java.util.*

/**
 * IncomeNotePresenter와 연결된 RecyclerView Adapter
 *
 * @author Yoon Jae-park
 * @since 2020.08
 */
class IncomeNoteListAdapter(
    private val incomeNotePresenter: IncomeNotePresenter
) : PagingSwipeAdapter<RespIncomeNoteInfo.IncomeNoteList, IncomeNoteListAdapter.ViewHolder>(
    DIFF_CALLBACK
) {
    private lateinit var mContext: Context

    private val moneySymbol = Currency.getInstance(Locale.KOREA).symbol
    private val swipeItemManger = SwipeItemRecyclerMangerImpl(this)

    inner class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        val txt_edit = view.findViewById<TextView>(R.id.txt_edit)
        val txt_delete = view.findViewById<TextView>(R.id.txt_delete)
        val txt_gain_data = view.findViewById<TextView>(R.id.txt_gain_data)
        val txt_subject_name = view.findViewById<TextView>(R.id.txt_subject_name)
        val txt_purchase_date_data = view.findViewById<TextView>(R.id.txt_purchase_date_data)
        val txt_sell_date_data = view.findViewById<TextView>(R.id.txt_sell_date_data)
        val txt_gain_percent_data = view.findViewById<TextView>(R.id.txt_gain_percent_data)
        val txt_purchase_price_data = view.findViewById<TextView>(R.id.txt_purchase_price_data)
        val txt_sell_price_data = view.findViewById<TextView>(R.id.txt_sell_price_data)
        val txt_sell_count_data = view.findViewById<TextView>(R.id.txt_sell_count_data)

        val swipeLayout_incomeNote = view.findViewById<SwipeLayout>(R.id.swipeLayout_incomeNote)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.item_income_note_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            bindDataList(holder, position)
            bindSwipeLayout(holder, position)
            incomeNotePresenter.requestRefreshTotalGainData()
        }
    }

    private fun bindDataList(holder: ViewHolder, position: Int) {
        if (getItem(position)==null) {
            return
        }
        holder.apply {
            txt_subject_name.isSelected = true
            txt_gain_data.isSelected = true
            txt_purchase_price_data.isSelected = true
            txt_sell_price_data.isSelected = true
            txt_gain_percent_data.isSelected = true

            //상단 데이터
            txt_subject_name.text = getItem(position)?.subjectName
            txt_gain_data.text = moneySymbol + getItem(position)?.realPainLossesAmount

            //왼쪽
            txt_sell_date_data.text = getItem(position)?.sellDate
            if (getItem(position)?.sellDate == "") {
                txt_sell_date_data.text = "-"
            }
            txt_gain_percent_data.text = "(${getItem(position)?.gainPercent})"
            //오른쪽
            txt_purchase_price_data.text = moneySymbol + getItem(position)?.purchasePrice
            txt_sell_price_data.text = moneySymbol + getItem(position)?.sellPrice
            txt_sell_count_data.text = getItem(position)?.sellCount.toString()

            var realPainLossesAmount = getItem(position)?.realPainLossesAmount?: 0.00

            if (realPainLossesAmount >= 0) {
                txt_gain_data.setTextColor(mContext.getColor(R.color.color_e52b4e))
                txt_gain_percent_data.setTextColor(mContext.getColor(R.color.color_e52b4e))
            } else {
                txt_gain_data.setTextColor(mContext.getColor(R.color.color_4876c7))
                txt_gain_percent_data.setTextColor(mContext.getColor(R.color.color_4876c7))
            }
        }
    }

    private fun bindSwipeLayout(holder: ViewHolder, position: Int) {
        swipeItemManger.bindView(holder.itemView, position)

        holder.apply {
            swipeLayout_incomeNote.addSwipeListener(object : SwipeLayout.SwipeListener {
                override fun onStartOpen(layout: SwipeLayout) {
                    swipeItemManger.closeAllExcept(layout)
                }
                override fun onOpen(layout: SwipeLayout) {}
                override fun onStartClose(layout: SwipeLayout) {}
                override fun onClose(layout: SwipeLayout) {}
                override fun onUpdate(layout: SwipeLayout, leftOffset: Int, topOffset: Int) {}
                override fun onHandRelease(layout: SwipeLayout, xvel: Float, yvel: Float) {}
            })
            txt_edit.setOnClickListener {
                incomeNotePresenter.onEditButtonClick(getItem(position))
                notifyDataSetChanged()
            }
            txt_delete.setOnClickListener {
                try {
                    CoroutineScope(Dispatchers.IO).launch {
                        incomeNotePresenter.onDeleteButtonClick(mContext, getItem(position)?.id!!)
                    }
                    notifyItemRemoved(position)
                    notifyItemRangeRemoved(position, itemCount)
                } catch (e: SQLException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun getSwipeLayoutResourceId(position: Int): Int {
        return R.id.swipeLayout_incomeNote
    }

    fun closeSwipeLayout() {
        swipeItemManger.closeAllItems()
    }

    companion object {
        private val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<RespIncomeNoteInfo.IncomeNoteList>() {
                override fun areItemsTheSame(
                    oldItem: RespIncomeNoteInfo.IncomeNoteList,
                    newItem: RespIncomeNoteInfo.IncomeNoteList
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: RespIncomeNoteInfo.IncomeNoteList,
                    newItem: RespIncomeNoteInfo.IncomeNoteList
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}