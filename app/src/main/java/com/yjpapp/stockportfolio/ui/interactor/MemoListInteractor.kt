package com.yjpapp.stockportfolio.ui.interactor

import android.content.Context
import com.yjpapp.stockportfolio.database.DatabaseController
import com.yjpapp.stockportfolio.database.Databases
import com.yjpapp.stockportfolio.database.data.MemoInfo

class MemoListInteractor {

    companion object {
        @Volatile private var instance: MemoListInteractor? = null
        private lateinit var mContext: Context
        private lateinit var databaseController: DatabaseController
        @JvmStatic
        fun getInstance(context: Context): MemoListInteractor =
                instance ?: synchronized(this) {
                    instance ?: MemoListInteractor().also {
                        instance = it
                        mContext = context
                        databaseController = DatabaseController.getInstance(mContext)
                    }
                }

    }

    fun getMemoInfo(id: Int): MemoInfo?{
        return databaseController.getMemoInfo(id)
    }

    fun getAllMemoInfoList(): ArrayList<MemoInfo?>{
        return databaseController.getAllMemoInfoList()
    }

    fun updateDeleteCheck(id: Int, deleteCheck: String){
        databaseController.updateDeleteCheck(id, deleteCheck)
    }

    fun deleteMemoInfoList(id: Int){
        databaseController.deleteData(id, Databases.TABLE_MEMO)
    }
}