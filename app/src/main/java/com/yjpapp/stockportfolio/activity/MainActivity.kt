package com.yjpapp.stockportfolio.activity

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
import kotlinx.android.synthetic.main.item_main_list.*
import kotlin.collections.ArrayList


class MainActivity : RootActivity(R.layout.activity_main), MainListAdapter.OnDeleteMode {

    private var isDeleteMode: Boolean = false
    private var mainListAdapter: MainListAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.logcat(Utils.getTodayYYYYMMDD())
        initLayout()
    }

    private fun initLayout(){
        recyclerview_MainActivity.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        val arrayList = ArrayList<DataInfo>()
        val dataInfo = DataInfo("세일", null, null, null, null, null, false)

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
        checkbox_all.setOnCheckedChangeListener {view, isChecked ->
            mainListAdapter?.setAllCheckClicked(isChecked)
            mainListAdapter?.notifyDataSetChanged()
        }
    }

    private val onClickListener = View.OnClickListener {view: View? ->
        when(view?.id){
            R.id.lin_bottom_menu_left -> {
                //delete 모드에서 취소 버튼을 누름
                if(isDeleteMode){
                    Toast.makeText(this@MainActivity, "deleteMode On!!!", Toast.LENGTH_SHORT).show()
                    lin_all_check.visibility = View.GONE
                    txt_bottom_menu_left.text = getString(R.string.common_modify)
                    txt_bottom_menu_right.text = getString(R.string.common_add)
                    mainListAdapter?.setDeleteModeOff()
                    mainListAdapter?.notifyDataSetChanged()
                    isDeleteMode = false
                }else{
                    Toast.makeText(this@MainActivity, "deleteMode Off!!!", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.lin_bottom_menu_right -> {
                //delete 모드에서 완료 버튼을 누름
                if(isDeleteMode){
                    Toast.makeText(this@MainActivity, "deleteMode On!!!", Toast.LENGTH_SHORT).show()
                    lin_all_check.visibility = View.GONE
                    txt_bottom_menu_left.text = getString(R.string.common_modify)
                    txt_bottom_menu_right.text = getString(R.string.common_add)
                    mainListAdapter?.setDeleteModeOff()
                    mainListAdapter?.notifyDataSetChanged()
                    isDeleteMode = false

                }else{

                }
            }
        }
    }
    override fun deleteModeOn() {
        isDeleteMode = true
        lin_all_check.visibility = View.VISIBLE
        txt_bottom_menu_left.text = getString(R.string.common_cancel)
        txt_bottom_menu_right.text = getString(R.string.common_complete)

        Utils.logcat("deleteModeOn 콜백 왔어!!")
    }

    // DB 생성 코드
    private fun createDB() {
        val dbHelper = DatabaseOpenHelper(this)
        val sqliteDatabases = dbHelper.writableDatabase
        val databaseHandler = DatabaseHandler(sqliteDatabases, dbHelper)

        val dataInfo = DataInfo("세일", null, null, null, null, null, false)
        dataInfo.gainPercent = "10%"
        dataInfo.realPainLossesAmount = "test"
        databaseHandler.insertData(dataInfo)
    }
    private fun deleteDataInfoList(){
        val dataInfoList = mainListAdapter?.getDataInfoList()
        for (i in 0..dataInfoList?.size!!){

        }
    }
}