package com.yjpapp.stockportfolio.ui.mystock

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.databinding.ItemMyStockListBinding

/**
 * @link MyStockFragment에 붙어있는 RecyclerView Adapter
 *
 * @author Yoon Jae-park
 * @since 2021.04
 */
class MyStockAdapter(private val myStockViewModel: MyStockViewModel): RecyclerView.Adapter<MyStockAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataBindingUtil.inflate<ItemMyStockListBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_my_stock_list,
                parent,
                false
        ).let {
            ViewHolder(it)
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mBinding.apply {
            myStockEntity = myStockViewModel.myStockInfoList.value?.get(position)
        }
        myStockViewModel.position = position
    }

    override fun getItemCount(): Int {
        myStockViewModel.myStockInfoList.value?.let {
            return it.size
        }
        return 0
    }

    inner class ViewHolder(binding: ItemMyStockListBinding) :
            RecyclerView.ViewHolder(binding.root) {
                val mBinding = binding
    }
}