package com.yjpapp.stockportfolio.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.database.data.IncomeNoteInfo
import com.yjpapp.stockportfolio.ui.presenter.IncomeNotePresenter
import com.yjpapp.stockportfolio.util.Utils
import kotlinx.android.synthetic.main.item_income_note_list.view.*
import java.sql.SQLException
import java.util.*

/**
 * IncomeNotePresenter와 연결된 RecyclerView Adapter
 *
 * @author Yun Jae-park
 * @since 2020.08
 */
class IncomeNoteListAdapter(val data: ArrayList<IncomeNoteInfo?>?, private val incomeNotePresenter: IncomeNotePresenter) :
        RecyclerView.Adapter<IncomeNoteListAdapter.ViewHolder>() {
    private lateinit var mContext: Context
    private var dataInfoList:MutableList<IncomeNoteInfo?> = mutableListOf()
    private var editModeOn: Boolean = false
    private var allCheckClick: Boolean = false
    private val moneySymbol = Currency.getInstance(Locale.KOREA).symbol

    init {
        if (data != null) {
            this.dataInfoList = data
        }
    }

    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.item_income_note_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // 아이템을 길게 눌렀을 때 편집모드로 전환.
        holder.itemView.apply {
            setOnLongClickListener {
                Utils.runVibration(mContext, 100)
                editModeOn = !isEditMode() //edit 모드가 꺼져있으면 키고, 켜져 있으면 끈다.
                notifyDataSetChanged()
                incomeNotePresenter.onAdapterItemLongClick(editModeOn)
                return@setOnLongClickListener true
            }
            txt_edit.setOnClickListener {
                incomeNotePresenter.onEditButtonClick(position)
                setEditMode(false)
                notifyDataSetChanged()
            }
            txt_delete.setOnClickListener {
                try {
                    val deleteIncomeNoteInfoId = dataInfoList[position]!!.id
                    incomeNotePresenter.onDeleteButtonClick(deleteIncomeNoteInfoId)
                    dataInfoList.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeRemoved(position, itemCount)
                } catch (e: SQLException) {
                    e.printStackTrace()
                }
            }
            txt_gain_data.isSelected = true

            bindDataList(holder, position)
            bindEditMode(holder)
        }
    }

    override fun getItemCount(): Int {
        return dataInfoList.size
    }

    fun deleteList(position: Int) {
        dataInfoList.removeAt(position)
    }

    fun getDataInfoList(): MutableList<IncomeNoteInfo?> {
        return dataInfoList
    }

    fun setDataInfoList(incomeNoteInfoList: MutableList<IncomeNoteInfo?>) {
        this.dataInfoList = incomeNoteInfoList
    }

    fun setEditMode(isEditMode: Boolean) {
        this.editModeOn = isEditMode
    }

    fun isEditMode(): Boolean {
        return editModeOn
    }

    private fun bindDataList(holder: ViewHolder, position: Int) {
        holder.apply {
            itemView.txt_subject_name.isSelected = true
            itemView.txt_gain_data.isSelected = true

            //상단 데이터
            itemView.txt_subject_name.text = dataInfoList[position]?.subjectName
            itemView.txt_gain_data.text = moneySymbol + dataInfoList[position]?.realPainLossesAmount
            //왼쪽
            itemView.txt_purchase_date_data.text = dataInfoList[position]?.purchaseDate
            itemView.txt_sell_date_data.text = dataInfoList[position]?.sellDate
            itemView.txt_gain_percent_data.text = "(" + dataInfoList[position]?.gainPercent + ")"
            //오른쪽
            itemView.txt_purchase_price_data.text = moneySymbol + dataInfoList[position]?.purchasePrice
            itemView.txt_sell_price_data.text = moneySymbol + dataInfoList[position]?.sellPrice
            itemView.txt_sell_count_data.text = dataInfoList[position]?.sellCount.toString()

            var realPainLossesAmountNumber = ""
            val realPainLossesAmountSplit = dataInfoList[position]?.realPainLossesAmount!!.split(",")
            for (i in realPainLossesAmountSplit.indices) {
                realPainLossesAmountNumber += realPainLossesAmountSplit[i]
            }
            if (realPainLossesAmountNumber.toDouble() >= 0) {
                itemView.txt_gain_data.setTextColor(mContext.getColor(R.color.color_e52b4e))
                itemView.txt_gain_percent_data.setTextColor(mContext.getColor(R.color.color_e52b4e))
            } else {
                itemView.txt_gain_data.setTextColor(mContext.getColor(R.color.color_4876c7))
                itemView.txt_gain_percent_data.setTextColor(mContext.getColor(R.color.color_4876c7))
            }
        }
    }

    private fun bindEditMode(holder: ViewHolder) {
        holder.apply {
            if (editModeOn) {
                itemView.lin_EditMode.visibility = View.VISIBLE
            } else {
                itemView.lin_EditMode.visibility = View.GONE
            }
        }

    }
}