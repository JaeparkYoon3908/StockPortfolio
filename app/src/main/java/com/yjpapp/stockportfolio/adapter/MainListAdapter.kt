package com.yjpapp.stockportfolio.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.model.DataInfo
import kotlinx.android.synthetic.main.item_data.*

class MainListAdapter(private val data: ArrayList<DataInfo>) :
    RecyclerView.Adapter<MainListAdapter.ViewHolder>(){
    private var mContext: Context? = null
    private var arrayList = ArrayList<DataInfo>()

    init {
        this.arrayList = data
    }

    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val inflater = mContext?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view:View = inflater.inflate(R.layout.item_data, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}