package com.yjpapp.stockportfolio.function.incomenote

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.swipe.SwipeLayout
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter
import com.daimajia.swipe.implments.SwipeItemRecyclerMangerImpl
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.localdb.sqlte.data.IncomeNoteInfo
import com.yjpapp.stockportfolio.model.IncomeNoteModel
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
) : PagingDataAdapter<IncomeNoteModel.IncomeNoteList, IncomeNoteListAdapter.ViewHolder>(DIFF_CALLBACK) {
    private lateinit var mContext: Context

    //    private var editModeOn: Boolean = false
//    private var allCheckClick: Boolean = false
    private val moneySymbol = Currency.getInstance(Locale.KOREA).symbol
//    private val swipeItemManger = SwipeItemRecyclerMangerImpl(this)

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

        //        val lin_EditMode = view.findViewById<LinearLayout>(R.id.lin_EditMode)
        val swipeLayout_incomeNote = view.findViewById<SwipeLayout>(R.id.swipeLayout_incomeNote)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.item_income_note_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // 아이템을 길게 눌렀을 때 편집모드로 전환.
        holder.apply {
//            itemView.setOnLongClickListener {
//                Utils.runVibration(mContext, 100)
//                editModeOn = !isEditMode() //edit 모드가 꺼져있으면 키고, 켜져 있으면 끈다.
//                notifyDataSetChanged()
//                incomeNotePresenter.onAdapterItemLongClick(editModeOn)
//                return@setOnLongClickListener true
//            }

//            txt_edit.setOnClickListener {
//                incomeNotePresenter.onEditButtonClick(position)
//                setEditMode(false)
//                notifyDataSetChanged()
//            }
//            txt_delete.setOnClickListener {
//                try {
//                    val deleteIncomeNoteInfoId = dataInfoList[position]!!.id
//                    incomeNotePresenter.onDeleteButtonClick(deleteIncomeNoteInfoId)
//                    dataInfoList.removeAt(position)
//                    notifyItemRemoved(position)
//                    notifyItemRangeRemoved(position, itemCount)
//                } catch (e: SQLException) {
//                    e.printStackTrace()
//                }
//            }
            bindDataList(holder, position)
//            bindEditMode(holder)
            bindSwipeLayout(holder, position)

        }
    }

    private fun bindDataList(holder: ViewHolder, position: Int) {
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
//            txt_purchase_date_data.text = dataInfoList[position]?.purchaseDate
            txt_sell_date_data.text = getItem(position)?.sellDate
            if (getItem(position)?.sellDate == "") {
                txt_sell_date_data.text = "-"
            }
            txt_gain_percent_data.text = "(${getItem(position)?.gainPercent})"
            //오른쪽
            txt_purchase_price_data.text = moneySymbol + getItem(position)?.purchasePrice
            txt_sell_price_data.text = moneySymbol + getItem(position)?.sellPrice
            txt_sell_count_data.text = getItem(position)?.sellCount.toString()

            var realPainLossesAmountNumber = ""
            val realPainLossesAmountSplit =
                getItem(position)?.realPainLossesAmount!!.split(",")
            for (i in realPainLossesAmountSplit.indices) {
                realPainLossesAmountNumber += realPainLossesAmountSplit[i]
            }
            if (realPainLossesAmountNumber.toDouble() >= 0) {
                txt_gain_data.setTextColor(mContext.getColor(R.color.color_e52b4e))
                txt_gain_percent_data.setTextColor(mContext.getColor(R.color.color_e52b4e))
            } else {
                txt_gain_data.setTextColor(mContext.getColor(R.color.color_4876c7))
                txt_gain_percent_data.setTextColor(mContext.getColor(R.color.color_4876c7))
            }
        }
    }

    //    private fun bindEditMode(holder: ViewHolder) {
//        holder.apply {
//            if (editModeOn) {
//                lin_EditMode.visibility = View.VISIBLE
//            } else {
//                lin_EditMode.visibility = View.GONE
//            }
//        }
//
//    }

    private fun bindSwipeLayout(holder: ViewHolder, position: Int) {
//        swipeItemManger.bindView(holder.itemView, position)

        holder.apply {
//            swipeLayout_incomeNote.addSwipeListener(object : SwipeLayout.SwipeListener {
//                override fun onStartOpen(layout: SwipeLayout) {
//                    swipeItemManger.closeAllExcept(layout)
//                }
//
//                override fun onOpen(layout: SwipeLayout) {}
//                override fun onStartClose(layout: SwipeLayout) {}
//                override fun onClose(layout: SwipeLayout) {}
//                override fun onUpdate(layout: SwipeLayout, leftOffset: Int, topOffset: Int) {}
//                override fun onHandRelease(layout: SwipeLayout, xvel: Float, yvel: Float) {}
//            })
            txt_edit.setOnClickListener {
                incomeNotePresenter.onEditButtonClick(getItem(position))
                notifyDataSetChanged()
            }
            txt_delete.setOnClickListener {
                try {
                    val deleteIncomeNoteInfoId = getItem(position)?.id!!
                    CoroutineScope(Dispatchers.IO).launch {
                        incomeNotePresenter.onDeleteButtonClick(mContext, getItem(position)?.id!!)
                    }
//                    dataInfoList.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeRemoved(position, itemCount)
                } catch (e: SQLException) {
                    e.printStackTrace()
                }
            }
        }
    }

//    override fun getItemCount(): Int {
//        return super.getItemCount()
//    }

//    override fun getSwipeLayoutResourceId(position: Int): Int {
//        return R.id.swipeLayout_incomeNote
//    }

//    fun deleteList(position: Int) {
//        dataInfoList.removeAt(position)
//    }
//
//    fun getDataInfoList(): MutableList<IncomeNoteInfo?> {
//        return dataInfoList
//    }

//    fun setEditMode(isEditMode: Boolean) {
//        this.editModeOn = isEditMode
//    }

    //    fun isEditMode(): Boolean {
//        return editModeOn
//    }
    fun closeSwipeLayout() {
//        swipeItemManger.closeAllItems()
    }

    companion object{
        private val DIFF_CALLBACK = object: DiffUtil.ItemCallback<IncomeNoteModel.IncomeNoteList>() {
            override fun areItemsTheSame(oldItem: IncomeNoteModel.IncomeNoteList, newItem: IncomeNoteModel.IncomeNoteList): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: IncomeNoteModel.IncomeNoteList, newItem: IncomeNoteModel.IncomeNoteList): Boolean {
                return oldItem == newItem
            }
        }
    }
}