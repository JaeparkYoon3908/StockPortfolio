package com.yjpapp.stockportfolio.ui.incomenote

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.database.data.IncomeNoteInfo
import com.yjpapp.stockportfolio.util.Utils
import java.sql.SQLException
import java.util.*

/**
 * IncomeNotePresenter와 연결된 RecyclerView Adapter
 *
 * @author Yoon Jae-park
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

    inner class ViewHolder(var view: View) : RecyclerView.ViewHolder(view){
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
        val lin_EditMode = view.findViewById<LinearLayout>(R.id.lin_EditMode)

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
            itemView.setOnLongClickListener {
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
            txt_subject_name.isSelected = true
            txt_gain_data.isSelected = true
            txt_purchase_price_data.isSelected = true
            txt_sell_price_data.isSelected = true
            txt_gain_percent_data.isSelected = true

            //상단 데이터
            txt_subject_name.text = dataInfoList[position]?.subjectName
            txt_gain_data.text = moneySymbol + dataInfoList[position]?.realPainLossesAmount
            //왼쪽
//            txt_purchase_date_data.text = dataInfoList[position]?.purchaseDate
            txt_sell_date_data.text = dataInfoList[position]?.sellDate
            if(dataInfoList[position]?.sellDate == ""){
                txt_sell_date_data.text = "-"
            }
            txt_gain_percent_data.text = "(" + dataInfoList[position]?.gainPercent + ")"
            //오른쪽
            txt_purchase_price_data.text = moneySymbol + dataInfoList[position]?.purchasePrice
            txt_sell_price_data.text = moneySymbol + dataInfoList[position]?.sellPrice
            txt_sell_count_data.text = dataInfoList[position]?.sellCount.toString()

            var realPainLossesAmountNumber = ""
            val realPainLossesAmountSplit = dataInfoList[position]?.realPainLossesAmount!!.split(",")
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

    private fun bindEditMode(holder: ViewHolder) {
        holder.apply {
            if (editModeOn) {
                lin_EditMode.visibility = View.VISIBLE
            } else {
                lin_EditMode.visibility = View.GONE
            }
        }

    }
}