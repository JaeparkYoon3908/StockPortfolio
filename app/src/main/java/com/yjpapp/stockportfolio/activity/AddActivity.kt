package com.yjpapp.stockportfolio.activity

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.adapter.AddListAdapter
import com.yjpapp.stockportfolio.adapter.MainListAdapter
import com.yjpapp.stockportfolio.model.DataInfo
import kotlinx.android.synthetic.main.activity_add.*

class AddActivity: RootActivity(R.layout.activity_add) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLayout()
    }
    private fun initLayout(){
        recyclerview_AddActivity.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        val arrayList = ArrayList<DataInfo>()
        val dataInfo = DataInfo("세일", null, null, null, null, null)
        arrayList.add(dataInfo)
        arrayList.add(dataInfo)
        arrayList.add(dataInfo)
        val mainListAdapter = AddListAdapter(arrayList)
        recyclerview_AddActivity.adapter = mainListAdapter
    }
    private val onClickListener = View.OnClickListener {v: View? ->
        when(v?.id){
            R.id.img_add ->{

            }

        }
    }
}