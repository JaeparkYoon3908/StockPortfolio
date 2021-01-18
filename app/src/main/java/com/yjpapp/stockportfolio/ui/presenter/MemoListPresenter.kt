package com.yjpapp.stockportfolio.ui.presenter

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.yjpapp.stockportfolio.database.data.MemoInfo
import com.yjpapp.stockportfolio.ui.activity.MemoReadWriteActivity
import com.yjpapp.stockportfolio.ui.adapter.MemoListAdapter
import com.yjpapp.stockportfolio.ui.fragment.MemoListFragment
import com.yjpapp.stockportfolio.ui.interactor.MemoListInteractor
import com.yjpapp.stockportfolio.ui.view.MemoListView
import com.yjpapp.stockportfolio.util.Utils

class MemoListPresenter(val mContext: Context, private val memoListView: MemoListView) {


    private val memoListInteractor = MemoListInteractor.getInstance(mContext)
    private lateinit var memoListAdapter: MemoListAdapter

    fun onResume(){
        val memoDataList = getAllMemoInfoList()
        memoListAdapter = MemoListAdapter(memoDataList, this)
        memoListView.setAdapter(memoListAdapter)
    }

    fun onAddButtonClicked(){
        val intent = Intent(mContext, MemoReadWriteActivity::class.java)
        intent.putExtra(MemoListFragment.INTENT_KEY_MEMO_MODE, MemoListFragment.MEMO_ADD_MODE)
        memoListView.startReadWriteActivityForResult(intent, MemoListFragment.REQUEST_ADD)
    }

    fun addMemoInfo(){
        val updateMemoList = memoListInteractor.getAllMemoInfoList()
        memoListAdapter.setMemoListData(updateMemoList)
//        memoListAdapter.notifyItemInserted(updateMemoList.size - 1)
        memoListAdapter.notifyDataSetChanged()
        if (updateMemoList.size != 0) {
            memoListView.hideGuideMessage()
        }
    }

    fun deleteMemoInfo(){
        val memoList = memoListInteractor.getAllMemoInfoList()
        for (position in memoList.indices) {
            if (memoList[position]?.deleteChecked!! == "true") {
                val id = memoList[position]!!.id
                memoListInteractor.deleteMemoInfoList(id)
                val updateMemoList = memoListInteractor.getAllMemoInfoList()
//                memoListView.deleteMemoListView(updateMemoList, position)
                memoListAdapter.setMemoListData(updateMemoList)
                memoListAdapter.notifyItemRemoved(position)
                memoListAdapter.notifyDataSetChanged()
                if (updateMemoList.size == 0) {
                    memoListView.showGuideMessage()
                }
            }
        }
        memoListAdapter.setDeleteModeOn(false)
        memoListAdapter.notifyDataSetChanged()

        memoListView.showAddButton()
        memoListView.hideDeleteButton()
        if (memoList.size == 0) {
            memoListView.showGuideMessage()
        }
    }

    fun updateMemoInfo(){
        val memoList = memoListInteractor.getAllMemoInfoList()
        memoListAdapter.setMemoListData(memoList)
        memoListAdapter.notifyDataSetChanged()
    }

    fun updateDeleteCheck(position: Int, deleteCheck: Boolean){
        val id = memoListInteractor.getAllMemoInfoList()[position]!!.id
        memoListInteractor.updateDeleteCheck(id, deleteCheck.toString())
    }

    fun getAllMemoInfoList(): ArrayList<MemoInfo?>{
       return memoListInteractor.getAllMemoInfoList()
    }

    fun onMemoListLongClicked(position: Int){
        if (!memoListAdapter.isDeleteModeOn()) {
            memoListView.hideAddButton()
            memoListView.showDeleteButton()
            updateDeleteCheck(position, true)
        } else {
            memoListView.showAddButton()
            memoListView.hideDeleteButton()
        }
        Utils.runVibration(mContext, 100)
        memoListAdapter.setDeleteModeOn(!memoListAdapter.isDeleteModeOn())
        val allMemoInfo = getAllMemoInfoList()
        memoListAdapter.setMemoListData(allMemoInfo)
        memoListAdapter.notifyDataSetChanged()
    }
    fun onMemoListClicked(position: Int){
        val memoDataList = getAllMemoInfoList()
        val intent = Intent(mContext, MemoReadWriteActivity::class.java)
        intent.putExtra(MemoListFragment.INTENT_KEY_LIST_POSITION, position)
        intent.putExtra(MemoListFragment.INTENT_KEY_MEMO_INFO_ID, memoDataList[position]?.id)
        intent.putExtra(MemoListFragment.INTENT_KEY_MEMO_INFO_TITLE, memoDataList[position]?.title)
        intent.putExtra(MemoListFragment.INTENT_KEY_MEMO_INFO_CONTENT,
                memoDataList[position]?.content)
        intent.putExtra(MemoListFragment.INTENT_KEY_MEMO_MODE, MemoListFragment.MEMO_READ_MODE)
        memoListView.startReadWriteActivityForResult(intent, MemoListFragment.REQUEST_READ)
    }
    fun clickMemoDeleteCheck(position: Int, deleteCheck: Boolean){
        updateDeleteCheck(position, deleteCheck)
    }

    fun onBackPressedClick(activity: Activity){
        if (memoListAdapter.isDeleteModeOn()) {
            memoListAdapter.setDeleteModeOn(false)
            memoListAdapter.notifyDataSetChanged()
            memoListView.showAddButton()
            memoListView.hideDeleteButton()
        } else {
            Utils.runBackPressAppCloseEvent(mContext, activity)
        }
    }
}