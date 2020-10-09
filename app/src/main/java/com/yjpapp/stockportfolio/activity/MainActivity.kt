package com.yjpapp.stockportfolio.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.adapter.MainListAdapter
import com.yjpapp.stockportfolio.database.DatabaseHandler
import com.yjpapp.stockportfolio.database.DatabaseOpenHelper
import kotlinx.android.synthetic.main.activity_main.*
import com.yjpapp.stockportfolio.model.DataInfo
import com.yjpapp.stockportfolio.util.Utils
import kotlin.collections.ArrayList


class MainActivity : RootActivity(R.layout.activity_main), MainListAdapter.OnDeleteMode {

    private var isDeleteMode: Boolean = false
    private var mainListAdapter: MainListAdapter? = null
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

        mainListAdapter = MainListAdapter(arrayList, this)
        recyclerview_MainActivity.adapter = mainListAdapter
        //img_back.setColorFilter(getColor(R.color.white))

        //리스너 등록
        //lin_add.setOnClickListener(onClickListener)
        lin_bottom_menu_left.setOnClickListener(onClickListener)
        lin_bottom_menu_right.setOnClickListener(onClickListener)
    }

    private val onClickListener = View.OnClickListener {view: View? ->
        when(view?.id){
            R.id.lin_bottom_menu_left -> {
                //delete 모드에서 왼쪽 버튼을 누름
                if(isDeleteMode){
                    Toast.makeText(this@MainActivity, "deleteMode On!!!", Toast.LENGTH_SHORT).show()
                    txt_bottom_menu_left.text = getString(R.string.common_modify)
                    txt_bottom_menu_right.text = getString(R.string.common_add)
                    mainListAdapter?.hideCheckBox()
                    mainListAdapter?.notifyDataSetChanged()
                    isDeleteMode = false
                }else{
                    Toast.makeText(this@MainActivity, "deleteMode Off!!!", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.lin_bottom_menu_right -> {
                //delete 모드에서 오른쪽 버튼을 누름
                if(isDeleteMode){
                    Toast.makeText(this@MainActivity, "deleteMode On!!!", Toast.LENGTH_SHORT).show()
                    txt_bottom_menu_left.text = getString(R.string.common_modify)
                    txt_bottom_menu_right.text = getString(R.string.common_add)
                    mainListAdapter?.hideCheckBox()
                    mainListAdapter?.notifyDataSetChanged()
                    isDeleteMode = false
                }else{

                }
            }
        }
    }

    // DB 생성 코드
    private fun createDB() {
        val dbHelper = DatabaseOpenHelper(this)
        val sqliteDatabases = dbHelper.writableDatabase
        val databaseHandler = DatabaseHandler(sqliteDatabases, dbHelper)

        val dataInfo = DataInfo("세일", null, null, null, null, null)
        dataInfo.gainPercent = "10%"
        dataInfo.realPainLossesAmount = "test"
        databaseHandler.insertData(dataInfo)
    }

    override fun deleteModeOn() {
        isDeleteMode = true
        txt_bottom_menu_left.text = getString(R.string.common_cancel)
        txt_bottom_menu_right.text = getString(R.string.common_complete)

        logcat("deleteModeOn 콜백 왔어!!")
    }
}