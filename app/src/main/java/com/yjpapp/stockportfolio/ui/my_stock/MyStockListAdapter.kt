package com.yjpapp.stockportfolio.ui.my_stock

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.database.model.IncomeNoteInfo
import com.yjpapp.stockportfolio.database.model.MyStockInfo

class MyStockListAdapter (private val allMySockList: ArrayList<MyStockInfo>) :
    RecyclerView.Adapter<MyStockListAdapter.ViewHolder>(){
    private var mContext: Context? = null

    inner class ViewHolder(var view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val inflater = mContext?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view:View = inflater.inflate(R.layout.item_my_stock_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return allMySockList.size
    }
}