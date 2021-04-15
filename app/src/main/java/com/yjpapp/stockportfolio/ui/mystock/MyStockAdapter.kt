package com.yjpapp.stockportfolio.ui.mystock

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.databinding.ItemMyStockListBinding

class MyStockAdapter(val myStockViewModel: MyStockViewModel): RecyclerView.Adapter<MyStockAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataBindingUtil.inflate<ItemMyStockListBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_my_stock_list,
                parent,
                false
        ).let {
            it.viewModel = myStockViewModel
            ViewHolder(it)
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mBinding.txtCurrentPrice.text = myStockViewModel.currentPrice.value
    }

    override fun getItemCount(): Int {
        return 5
    }

    inner class ViewHolder(private val binding: ItemMyStockListBinding) :
            RecyclerView.ViewHolder(binding.root) {
                val mBinding = binding
    }
}