package com.yjpapp.stockportfolio.ui.mystock

import android.content.Context
import android.content.Intent
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.database.room.MyStockEntity
import com.yjpapp.stockportfolio.databinding.ItemMyStockListBinding
import com.yjpapp.stockportfolio.ui.main.MainActivity
import java.util.logging.Handler


/**
 * @link MyStockFragment에 붙어있는 RecyclerView Adapter
 *
 * @author Yoon Jae-park
 * @since 2021.04
 */
class MyStockAdapter(private var myStockList: MutableList<MyStockEntity>): RecyclerView.Adapter<MyStockAdapter.ViewHolder>() {
    private lateinit var adapterCallBack: AdapterCallBack

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
            myStockEntity = myStockList[position]
            callBack = adapterCallBack
        }
    }

    override fun getItemCount(): Int {
        return myStockList.size
    }

    inner class ViewHolder(binding: ItemMyStockListBinding) :
            RecyclerView.ViewHolder(binding.root) {
                val mBinding = binding
    }

    fun setMyStockList(myStockList: MutableList<MyStockEntity>){
        this.myStockList = myStockList
        notifyDataSetChanged()
    }

    fun setCallBack(callBack: AdapterCallBack){
        this.adapterCallBack = callBack
    }

    interface AdapterCallBack{
        fun onEditClick(myStockEntity: MyStockEntity?)
        fun onSellClick(myStockEntity: MyStockEntity?)
        fun onDeleteClick(myStockEntity: MyStockEntity?)
    }
}