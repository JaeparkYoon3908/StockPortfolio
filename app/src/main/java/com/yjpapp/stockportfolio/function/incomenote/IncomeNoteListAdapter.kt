package com.yjpapp.stockportfolio.function.incomenote

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.dialog.CommonTwoBtnDialog
import com.yjpapp.stockportfolio.localdb.preference.PrefKey
import com.yjpapp.stockportfolio.localdb.preference.PreferenceController
import com.yjpapp.stockportfolio.model.response.RespIncomeNoteInfo
import com.yjpapp.stockportfolio.util.Utils
import com.yjpapp.swipelayout.SwipeLayout
import com.yjpapp.swipelayout.adapters.PagingSwipeAdapter
import com.yjpapp.swipelayout.implments.SwipeItemRecyclerMangerImpl
import kotlinx.coroutines.launch
import java.math.BigDecimal
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
        }
    }

    private fun bindDataList(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.apply {
                txt_subject_name.isSelected = true
                txt_gain_data.isSelected = true
                txt_purchase_price_data.isSelected = true
                txt_sell_price_data.isSelected = true
                txt_gain_percent_data.isSelected = true

                //회사 이름
                txt_subject_name.text = it.subjectName
                //수익
                txt_gain_data.text = moneySymbol + Utils.getNumInsertComma(BigDecimal(it.realPainLossesAmount).toString())
                //수익 퍼센트
                txt_gain_percent_data.text = "(${it.gainPercent}%)"
                //매도일
                txt_sell_date_data.text = getItem(position)?.sellDate
                if (getItem(position)?.sellDate == "") {
                    txt_sell_date_data.text = "-"
                }
                //매수금액
                txt_purchase_price_data.text = moneySymbol + Utils.getNumInsertComma(BigDecimal(it.purchasePrice).toString())
                //매도금액
                txt_sell_price_data.text = moneySymbol + Utils.getNumInsertComma(BigDecimal(it.sellPrice).toString())
                //매도수량
                txt_sell_count_data.text = Utils.getNumInsertComma(it.sellCount.toString())

                val realPainLossesAmount = it.realPainLossesAmount
                if (realPainLossesAmount >= 0) {
                    txt_gain_data.setTextColor(mContext.getColor(R.color.color_e52b4e))
                    txt_gain_percent_data.setTextColor(mContext.getColor(R.color.color_e52b4e))
                } else {
                    txt_gain_data.setTextColor(mContext.getColor(R.color.color_4876c7))
                    txt_gain_percent_data.setTextColor(mContext.getColor(R.color.color_4876c7))
                }
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
            }
            txt_delete.setOnClickListener {
                val isShowDeleteCheck = PreferenceController.getInstance(mContext).getPreference(PrefKey.KEY_SETTING_INCOME_NOTE_SHOW_DELETE_CHECK)?:"true"
                if (isShowDeleteCheck == "true") {
                    CommonTwoBtnDialog(
                        mContext,
                        CommonTwoBtnDialog.CommonTwoBtnData(
                            noticeText = "삭제하시겠습니까?",
                            leftBtnText = mContext.getString(R.string.Common_Cancel),
                            rightBtnText = mContext.getString(R.string.Common_Ok),
                            leftBtnListener = object : CommonTwoBtnDialog.OnClickListener {
                                override fun onClick(view: View, dialog: CommonTwoBtnDialog) {
                                    dialog.dismiss()
                                }
                            },
                            rightBtnListener = object : CommonTwoBtnDialog.OnClickListener {
                                override fun onClick(view: View, dialog: CommonTwoBtnDialog) {
//                                    notifyItemRemoved(position)
//                                    notifyItemRangeRemoved(position, itemCount)
                                    incomeNotePresenter.onDeleteOkClick(mContext, getItem(position)?.id!!, position)
                                    dialog.dismiss()
                                }
                            }
                        )
                    ).show()
                } else {
//                    notifyItemRemoved(position)
//                    notifyItemRangeRemoved(position, itemCount)
                    incomeNotePresenter.onDeleteOkClick(mContext, getItem(position)?.id!!, position)
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