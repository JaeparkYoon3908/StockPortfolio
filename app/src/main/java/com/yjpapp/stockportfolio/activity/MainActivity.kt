package com.yjpapp.stockportfolio.activity

import android.os.Bundle
import android.view.View
import android.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.adapter.MainListAdapter
import com.yjpapp.stockportfolio.database.DatabaseHandler
import com.yjpapp.stockportfolio.database.DatabaseOpenHelper
import kotlinx.android.synthetic.main.activity_main.*
import com.yjpapp.stockportfolio.model.DataInfo
import com.yjpapp.stockportfolio.util.Utils


class MainActivity : RootActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logcat(Utils.getTodayYYYYMMDD())
        initLayout()
    }

    private fun initLayout(){
        rv_data.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        val arrayList = ArrayList<DataInfo>()
        val dataInfo = DataInfo("세일", null, null, null, null, null)
        arrayList.add(dataInfo)
        arrayList.add(dataInfo)
        arrayList.add(dataInfo)
        val mainListAdapter = MainListAdapter(arrayList)
        rv_data.adapter = mainListAdapter
        img_back.setColorFilter(getColor(R.color.white))
        img_back.setOnClickListener{

        }
    }

    // DB 생성 코드
    private fun createDB(){
        val dbHelper = DatabaseOpenHelper(this)
        val sqliteDatabases = dbHelper.writableDatabase
        val databaseHandler = DatabaseHandler(sqliteDatabases, dbHelper)

        val dataInfo = DataInfo("세일", null, null, null, null, null)
        dataInfo.gainPercent = "10%"
        dataInfo.realPainLossesAmount = "test"
        databaseHandler.insertData(dataInfo)
    }
}