package com.yjpapp.stockportfolio.function.incomenote

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.databinding.ItemIncomeNoteListBinding
import com.yjpapp.stockportfolio.dialog.CommonTwoBtnDialog
import com.yjpapp.stockportfolio.localdb.preference.PrefKey
import com.yjpapp.stockportfolio.localdb.preference.PreferenceController
import com.yjpapp.stockportfolio.model.response.RespIncomeNoteInfo
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
    var incomeNoteList: ArrayList<RespIncomeNoteInfo.IncomeNoteList>,
    var callBack: CallBack?
) : RecyclerSwipeAdapter<IncomeNoteListAdapter.IncomeNoteListViewHolder>()
{
    private lateinit var mContext: Context

    private val swipeItemManger = SwipeItemRecyclerMangerImpl(this)

    inner class IncomeNoteListViewHolder(var binding: ItemIncomeNoteListBinding) : RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncomeNoteListViewHolder {
        mContext = parent.context
        val binding: ItemIncomeNoteListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_income_note_list,
            parent,
            false
        )
        return IncomeNoteListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IncomeNoteListViewHolder, position: Int) {
        holder.apply {
            bindDataList(holder, position)
            bindSwipeLayout(holder, position)
        }
    }

    private fun bindDataList(holder: IncomeNoteListViewHolder, position: Int) {
        holder.binding.apply {
            incomeNote = incomeNoteList[position]
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
//                incomeNotePresenter.onEditButtonClick(getItem(position))
                callBack?.onEditButtonClick(incomeNoteList[position])
            }
            txtDelete.setOnClickListener {
                val isShowDeleteCheck = PreferenceController.getInstance(mContext)
                    .getPreference(PrefKey.KEY_SETTING_INCOME_NOTE_SHOW_DELETE_CHECK) ?: "true"
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
                                    if (itemCount > position) {
                                        callBack?.onDeleteOkClick(
                                            incomeNoteList[position].id,
                                            position
                                        )
                                        dialog.dismiss()
                                    } else {
                                        Toast.makeText(
                                            mContext,
                                            "position = $position ItemCount = $itemCount",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        )
                    ).show()
                } else {
                    callBack?.onDeleteOkClick(
                        incomeNoteList[position].id,
                        position
                    )
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

    interface CallBack {
        fun onEditButtonClick(respIncomeNoteList: RespIncomeNoteInfo.IncomeNoteList?)
        fun onDeleteOkClick(id: Int, position: Int)
    }

    override fun getItemCount(): Int {
        return incomeNoteList.size
    }
}