package com.yjpapp.stockportfolio.ui.mystock

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
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
            it.viewModel = myStockViewModel
            ViewHolder(it)
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        myStockViewModel.position = position
        holder.mBinding.apply {
            txtCompany.isSelected = true
            txtCurrentPrice.isSelected = true
            txtChangePricePercent.isSelected = true
            txtPurchasePrice.isSelected = true
            txtPurchaseCount.isSelected = true
            txtGainData.isSelected = true
            txtGainPercentData.isSelected = true
        }
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