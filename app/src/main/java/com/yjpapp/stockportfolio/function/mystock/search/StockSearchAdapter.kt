package com.yjpapp.stockportfolio.function.mystock.search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.databinding.ItemIncomeNoteListBinding

/**
 * @link StockSearchFragment 붙어있는 RecyclerView Adapter
 *
 * @author Yoon Jae-park
 * @since 2021.11
 */
class StockSearchAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mContext = parent.context
        val binding: ItemIncomeNoteListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_income_note_list,
            parent,
            false
        )
        return IncomeNoteListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")

    }

    override fun getItemCount(): Int {
        return 1
    }

    inner class IncomeNoteListViewHolder(var binding: ItemIncomeNoteListBinding) : RecyclerView.ViewHolder(binding.root) {}

}