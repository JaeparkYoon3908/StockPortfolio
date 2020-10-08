package com.yjpapp.stockportfolio.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.adapter.MainListAdapter
import com.yjpapp.stockportfolio.database.DatabaseHandler
import com.yjpapp.stockportfolio.database.DatabaseOpenHelper
import kotlinx.android.synthetic.main.activity_main.*
import com.yjpapp.stockportfolio.model.DataInfo
import com.yjpapp.stockportfolio.util.Utils
import kotlin.collections.ArrayList


class MainActivity : RootActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logcat(Utils.getTodayYYYYMMDD())
        initLayout()
    }

    private fun initLayout(){
        recyclerview_MainActivity.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        val arrayList = ArrayList<DataInfo>()
        val dataInfo = DataInfo("세일", null, null, null, null, null)

        for(i in 1..10){
            arrayList.add(dataInfo)
        }

        val mainListAdapter = MainListAdapter(arrayList)
        recyclerview_MainActivity.adapter = mainListAdapter
        //img_back.setColorFilter(getColor(R.color.white))

        //리스너 등록
        //lin_add.setOnClickListener(onClickListener)

    }

    private val onClickListener = View.OnClickListener {view: View? ->
        when(view?.id){
            /*
            R.id.lin_add -> {
                val intent = Intent(mContext, AddActivity::class.java)
                startActivity(intent)
            }
             */

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