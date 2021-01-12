package com.yjpapp.stockportfolio.ui.presenter

import android.content.Context
import com.yjpapp.stockportfolio.database.data.MyStockInfo
import com.yjpapp.stockportfolio.ui.adapter.MyStockListAdapter
import com.yjpapp.stockportfolio.ui.interactor.MyStockInteractor
import com.yjpapp.stockportfolio.ui.view.MyStockView

class MyStockPresenter(val mContext: Context, private val myStockView: MyStockView) {
    private lateinit var myStockListAdapter: MyStockListAdapter
    private val myStockInteractor = MyStockInteractor.getInstance(mContext)
    private var myStockId = -1
    private var editMode = false

    fun onResume(){
        val myStockList = myStockInteractor.getAllMyStockList()
        myStockListAdapter = MyStockListAdapter(myStockList, this)
        myStockView.setAdapter(myStockListAdapter)
    }

    fun onMyStockListLongClick(){
        myStockListAdapter.setEditModeOn(!myStockListAdapter.isEditModeOn())
        myStockListAdapter.notifyDataSetChanged()
    }

    fun onInputDialogCompleteClicked(myStockInfo: MyStockInfo) {
        //id 설정
        myStockInfo.id = myStockId

//        if (editMode) {
//            incomeNoteInteractor.updateIncomeNoteInfo(myStockInfo)
//            val newDataList = incomeNoteInteractor.getAllIncomeNoteInfoList()
//            incomeNoteListAdapter.setDataInfoList(newDataList)
//            incomeNoteListAdapter.notifyDataSetChanged()
//            incomeNoteView.showAddButton()
//            incomeNoteView.scrollTopPosition(newDataList.size - 1)
//            incomeNoteView.bindTotalGainData()
//        } else {
//            incomeNoteInteractor.insertIncomeNoteInfo(incomeNoteInfo)
//            val newDataList = incomeNoteInteractor.getAllIncomeNoteInfoList()
//            incomeNoteListAdapter.setDataInfoList(newDataList)
//            incomeNoteListAdapter.notifyItemInserted(incomeNoteListAdapter.itemCount - 1)
//            incomeNoteView.scrollTopPosition(newDataList.size - 1)
//            incomeNoteView.showAddButton()
//            incomeNoteView.bindTotalGainData()
//        }
    }

    //툴바 Add버튼 클릭
    fun onAddButtonClick(){
        myStockId = -1     
    }

    //편집 버튼 클릭
    fun onEditButtonClick(position: Int){

    }

    //삭제 버튼 클릭
    fun onDeleteButtonClick(){

    }

    //매도 버튼 클릭
    fun onSellButtonClick(position: Int){

    }
}