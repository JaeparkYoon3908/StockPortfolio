package com.yjpapp.stockportfolio.test

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.databinding.ItemTestBinding

class TestRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mContext = parent.context
        val binding: ItemTestBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_test,
            parent,
            false
        )
        return TestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 1
    }

    inner class TestViewHolder(var binding: ItemTestBinding) : RecyclerView.ViewHolder(binding.root) {}

}