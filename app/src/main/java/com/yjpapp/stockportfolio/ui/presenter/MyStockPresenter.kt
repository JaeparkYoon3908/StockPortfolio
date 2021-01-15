package com.yjpapp.stockportfolio.ui.presenter

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.database.data.MyStockInfo
import com.yjpapp.stockportfolio.ui.adapter.MyStockListAdapter
import com.yjpapp.stockportfolio.ui.interactor.MyStockInteractor
import com.yjpapp.stockportfolio.ui.service.MyStockService
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
        startService()
    }

    fun onDestroy(){
        stopService()
    }

    fun onAllFilterClicked() {
        val allMyStockList = myStockInteractor.getAllMyStockList()
        myStockListAdapter.setMyStockList(allMyStockList)
        myStockListAdapter.notifyDataSetChanged()
        val text = mContext.getString(R.string.Common_All)
        myStockView.changeFilterText(text)
    }

    fun onGainFilterClicked() {
//        val gainDataList = incomeNoteInteractor.getGainIncomeNoteInfoList()
//        myStockListAdapter.setDataInfoList(gainDataList)
        myStockListAdapter.notifyDataSetChanged()
        val text = mContext.getString(R.string.Common_Gain)
        myStockView.changeFilterText(text)
    }

    fun onLossFilterClicked() {
//        val lossDataList = incomeNoteInteractor.getAllIncomeNoteInfoList()
//        myStockListAdapter.setDataInfoList(gainDataList)
        myStockListAdapter.notifyDataSetChanged()
        val text = mContext.getString(R.string.Common_Loss)
        myStockView.changeFilterText(text)
    }

    fun onMyStockListLongClick(){
        myStockListAdapter.setEditMode(!myStockListAdapter.isEditMode())
        myStockListAdapter.notifyDataSetChanged()
        if(myStockListAdapter.isEditMode()){
            myStockView.hideAddButton()
        }else{
            myStockView.showAddButton()
        }
    }

    fun onInputDialogCompleteClicked(myStockInfo: MyStockInfo) {
        //id 설정
        myStockInfo.id = myStockId
        if(editMode){
            editMode = false
            myStockInteractor.updateMyStockInfo(myStockInfo)
            val newMyStockInfoList = myStockInteractor.getAllMyStockList()
            myStockListAdapter.setMyStockList(newMyStockInfoList)
            myStockListAdapter.setEditMode(false)
            myStockListAdapter.notifyDataSetChanged()
            myStockView.showAddButton()
            myStockView.scrollTopPosition(newMyStockInfoList.size - 1)
            myStockView.bindTotalGainData()
        }else{
            myStockInteractor.insertMyStockInfo(myStockInfo)
            val newMyStockInfoList = myStockInteractor.getAllMyStockList()
            myStockListAdapter.setMyStockList(newMyStockInfoList)
            myStockListAdapter.notifyItemInserted(myStockListAdapter.itemCount - 1)
            myStockView.scrollTopPosition(newMyStockInfoList.size - 1)
            myStockView.showAddButton()
            myStockView.bindTotalGainData()
        }
    }

    //툴바 Add버튼 클릭
    fun onAddButtonClick(){
        myStockId = -1
        editMode = false
    }

    //툴바 편집버튼 클릭
    fun onMenuEditButtonClick() {
        val allMyStockInfo = myStockInteractor.getAllMyStockList()
        if (allMyStockInfo.size > 0) {
            myStockListAdapter.setEditMode(!myStockListAdapter.isEditMode())
            myStockListAdapter.notifyDataSetChanged()
            if (myStockListAdapter.isEditMode()) {
                myStockView.hideAddButton()
            } else {
                myStockView.showAddButton()
            }
        }
    }

    //편집 버튼 클릭
    fun onEditButtonClick(position: Int){
        val myStockInfo = myStockInteractor.getAllMyStockList()
        myStockId = myStockInfo[position]!!.id
        editMode = true
        myStockView.showInputDialog(editMode, myStockInfo[position])
    }

    //삭제 버튼 클릭
    fun onDeleteButtonClick(position: Int) {
        val allMyStockInfo = myStockInteractor.getAllMyStockList()
        val id = allMyStockInfo[position]!!.id
        myStockInteractor.deleteMyStockInfo(id)
        val updateAllMyStockInfo = myStockInteractor.getAllMyStockList()
        myStockListAdapter.setMyStockList(updateAllMyStockInfo)
        myStockListAdapter.notifyItemRemoved(position)
        myStockListAdapter.notifyItemRangeRemoved(position, myStockListAdapter.itemCount)
        myStockView.bindTotalGainData()
        if (updateAllMyStockInfo.size == 0) {
            myStockView.showAddButton()
        }
    }

    //매도 버튼 클릭
    fun onSellButtonClick(position: Int){

    }

    private var mIsBound = false
    private fun startService(){
        val intent = Intent(mContext, MyStockService::class.java)
        mContext.startService(intent)
        mContext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
    }
    private fun stopService(){
        if(mIsBound){
            mContext.unbindService(mConnection)
            mIsBound = false
        }
        val intent = Intent(mContext, MyStockService::class.java)
        mContext.stopService(intent)
    }

    private var mServiceMessenger: Messenger? = null
    private val mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            Log.d("test", "onServiceConnected")
            mServiceMessenger = Messenger(iBinder)
            try {
                val msg: Message = Message.obtain(null, MyStockService.MSG_REGISTER_CLIENT)
                msg.replyTo = mMessenger
                mServiceMessenger!!.send(msg)
            } catch (e: RemoteException) {

            }
        }
        override fun onServiceDisconnected(componentName: ComponentName) {}
    }

    /** Service 로 부터 message를 받음  */
    private val mMessenger: Messenger = Messenger(Handler(Looper.getMainLooper()) { msg ->
        Log.w("test", "ControlService - message what : " + msg.what + " , msg.obj " + msg.obj)
        when (msg.what) {
            MyStockService.MSG_SEND_TO_ACTIVITY -> {
                val currentPrice = msg.data.getString("currentPrice")
                val allMyStockList = myStockInteractor.getAllMyStockList()
                if(currentPrice != null){
                    myStockInteractor.updateCurrentPrice(allMyStockList[0]!!.id, currentPrice)
                }
                val updateMyStockList = myStockInteractor.getAllMyStockList()
                myStockListAdapter.setMyStockList(updateMyStockList)
                myStockListAdapter.notifyDataSetChanged()
                val newMsg: Message = Message.obtain(null, MyStockService.MSG_DATA_REQUEST)
                mServiceMessenger!!.send(newMsg)
            }
        }
        false
    })

    /** Service 로 메시지를 보냄  */
    private fun sendMessageToService(str: String) {
        if (mIsBound) {
            if (mServiceMessenger != null) {
                try {
                    val msg: Message = Message.obtain(null, MyStockService.MSG_SEND_TO_SERVICE, str)
                    msg.replyTo = mMessenger
                    mServiceMessenger!!.send(msg)
                } catch (e: RemoteException) {

                }
            }
        }
    }
}