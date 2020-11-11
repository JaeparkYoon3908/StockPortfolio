package com.yjpapp.stockportfolio.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.adapter.MainListAdapter
import com.yjpapp.stockportfolio.model.DataInfo
import com.yjpapp.stockportfolio.util.Utils
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : RootActivity(R.layout.activity_main), MainListAdapter.OnDeleteMode {

    private var isDeleteMode: Boolean = false
    private var mainListAdapter: MainListAdapter? = null
    private var dataList: ArrayList<DataInfo?>? = null
    private var modeType: Int = 0

//    private lateinit var databaseHandler:DatabaseHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.logcat(Utils.getTodayYYYYMMDD())
        initLayout()
//        insertData()
    }

    private fun initLayout(){
        recyclerview_MainActivity.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
//        val arrayList = ArrayList<DataInfo>()
        val dataInfo = DataInfo(0,"세일", null, null, null, null, null, null)

        dataList  = databaseHandler?.getAllDataInfo()
        mainListAdapter = MainListAdapter(dataList, this)

        recyclerview_MainActivity.adapter = mainListAdapter

        lin_bottom_menu_left.setOnClickListener(onClickListener)
        lin_bottom_menu_right.setOnClickListener(onClickListener)
        checkbox_all.setOnCheckedChangeListener {view, isChecked ->
            mainListAdapter?.setAllCheckClicked(isChecked)
            mainListAdapter?.notifyDataSetChanged()
        }
    }

    /**mode type
     * 0 : view mode (default, 수정, 추가)
     * 1 : delete mode (삭제 모드, 취소, 완료)
     * 2 : add mode (추가 모드, 취소, 완료)
     * 3 : modify mode (수정 모드, 취소, 완료)
     */
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
//                    var dataList = mainListAdapter?.getDataInfoList()!!

                    for(i in 0 until dataList!!.size){
                        if(dataList?.get(i)?.isDeleteCheck.equals(getString(R.string.common_true))){
                            val position: Int = dataList?.get(i)?.id!!
                            databaseHandler?.deleteDataInfo(position)
                        }
                    }
                    dataList = databaseHandler?.getAllDataInfo()!!
                    mainListAdapter?.setDataInfoList(dataList!!)
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


    private fun insertData() {

        val dataInfo = DataInfo(0,"세일", null, null, null, null, null, null)
        dataInfo.gainPercent = "10%"
        dataInfo.realPainLossesAmount = "test"
        for(i in 0 until 10){
            databaseHandler?.insertData(dataInfo)
        }
    }
}