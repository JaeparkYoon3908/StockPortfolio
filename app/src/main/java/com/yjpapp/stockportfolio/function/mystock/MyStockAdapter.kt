package com.yjpapp.stockportfolio.function.mystock

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.swipe.SwipeLayout
import com.daimajia.swipe.SwipeLayout.SwipeListener
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter
import com.daimajia.swipe.implments.SwipeItemRecyclerMangerImpl
import com.daimajia.swipe.interfaces.SwipeAdapterInterface
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.constance.StockPortfolioConfig
import com.yjpapp.stockportfolio.localdb.room.MyStockEntity
import com.yjpapp.stockportfolio.databinding.ItemMyStockListBinding


/**
 * @link MyStockFragment에 붙어있는 RecyclerView Adapter
 *
 * @author Yoon Jae-park
 * @since 2021.04
 */
class MyStockAdapter(private var myStockList: MutableList<MyStockEntity>): RecyclerSwipeAdapter<MyStockAdapter.ViewHolder>(), SwipeAdapterInterface {
    private lateinit var adapterCallBack: AdapterCallBack
    private val swipeItemManger = SwipeItemRecyclerMangerImpl(this)

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
        swipeItemManger.bindView(holder.itemView, position)
        holder.mBinding.apply {
            //dataBinding
            myStockEntity = myStockList[position]
            callBack = adapterCallBack
            pos = position
            moneySymbol = StockPortfolioConfig.moneySymbol
            //init
            swipeLayoutMyStock.addSwipeListener(object : SwipeListener {
                override fun onStartOpen(layout: SwipeLayout) {
                    swipeItemManger.closeAllExcept(layout)
                }
                override fun onOpen(layout: SwipeLayout) {}
                override fun onStartClose(layout: SwipeLayout) {}
                override fun onClose(layout: SwipeLayout) {}
                override fun onUpdate(layout: SwipeLayout, leftOffset: Int, topOffset: Int) {}
                override fun onHandRelease(layout: SwipeLayout, xvel: Float, yvel: Float) {}
            })
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
//        notifyDataSetChanged()
    }

    fun closeSwipeLayout(){
        swipeItemManger.closeAllItems()
    }

    fun setCallBack(callBack: AdapterCallBack){
        this.adapterCallBack = callBack
    }

    interface AdapterCallBack{
        fun onEditClick(myStockEntity: MyStockEntity?)
        fun onSellClick(myStockEntity: MyStockEntity?)
        fun onDeleteClick(myStockEntity: MyStockEntity?, position: Int)
    }

    override fun getSwipeLayoutResourceId(position: Int): Int {
        return R.id.swipeLayoutMyStock
    }
}