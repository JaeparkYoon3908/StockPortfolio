package com.yjpapp.swipelayout.adapters

import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yjpapp.swipelayout.SwipeLayout
import com.yjpapp.swipelayout.implments.SwipeItemMangerImpl
import com.yjpapp.swipelayout.implments.SwipeItemRecyclerMangerImpl
import com.yjpapp.swipelayout.interfaces.SwipeAdapterInterface
import com.yjpapp.swipelayout.interfaces.SwipeItemMangerInterface
import com.yjpapp.swipelayout.util.Attributes

abstract class PagingSwipeAdapter<T : Any, VH : RecyclerView.ViewHolder>(diffCallBack: DiffUtil.ItemCallback<T>) :
    PagingDataAdapter<T, VH>(diffCallBack), SwipeItemMangerInterface, SwipeAdapterInterface {
    var mItemManger = SwipeItemRecyclerMangerImpl(this)

    abstract override fun onBindViewHolder(viewHolder: VH, position: Int)

    override fun openItem(position: Int) {
        mItemManger.openItem(position)
    }

    override fun closeItem(position: Int) {
        mItemManger.closeItem(position)
    }

    override fun closeAllExcept(layout: SwipeLayout?) {
        mItemManger.closeAllExcept(layout)
    }

    override fun closeAllItems() {
        mItemManger.closeAllItems()
    }

    override fun getOpenItems(): List<Int?>? {
        return mItemManger.openItems
    }

    override fun getOpenLayouts(): List<SwipeLayout?>? {
        return mItemManger.openLayouts
    }

    override fun removeShownLayouts(layout: SwipeLayout?) {
        mItemManger.removeShownLayouts(layout)
    }

    override fun isOpen(position: Int): Boolean {
        return mItemManger.isOpen(position)
    }

    override fun getMode(): Attributes.Mode? {
        return mItemManger.mode
    }

    override fun setMode(mode: Attributes.Mode?) {
        mItemManger.mode = mode
    }
}