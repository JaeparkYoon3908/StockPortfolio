package com.yjpapp.stockportfolio.ui.interactor

import android.content.Context
import com.yjpapp.stockportfolio.database.DatabaseController
import com.yjpapp.stockportfolio.database.data.MemoInfo

/**
 * MemoReadWriteActivity의 Model 역할하는 class
 *
 * @author Yun Jae-park
 * @since 2020.12
 */
class MemoReadWriteInteractor {

    companion object {
        @Volatile private var instance: MemoReadWriteInteractor? = null
        private lateinit var mContext: Context
        private lateinit var databaseController: DatabaseController
        @JvmStatic
        fun getInstance(context: Context): MemoReadWriteInteractor =
                instance ?: synchronized(this) {
                    instance ?: MemoReadWriteInteractor().also {
                        instance = it
                        mContext = context
                        databaseController = DatabaseController.getInstance(mContext)
                    }
                }

    }
    fun insertMemoData(memoData: MemoInfo){
        databaseController.insertMemoData(memoData)
    }
    fun updateMemoData(memoData: MemoInfo){
        databaseController.updateMemoData(memoData)
    }
}