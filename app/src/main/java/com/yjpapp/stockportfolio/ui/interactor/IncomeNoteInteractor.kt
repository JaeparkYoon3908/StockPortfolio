package com.yjpapp.stockportfolio.ui.interactor

import android.content.Context
import com.yjpapp.stockportfolio.database.DatabaseController
import com.yjpapp.stockportfolio.database.Databases
import com.yjpapp.stockportfolio.database.data.IncomeNoteInfo

class IncomeNoteInteractor {

    companion object {
        @Volatile private var instance: IncomeNoteInteractor? = null
        private lateinit var mContext: Context
        private lateinit var databaseController: DatabaseController
        @JvmStatic
        fun getInstance(context: Context): IncomeNoteInteractor =
                instance ?: synchronized(this) {
                    instance ?: IncomeNoteInteractor().also {
                        instance = it
                        mContext = context
                        databaseController = DatabaseController.getInstance(mContext)
                    }
                }

    }
    fun insertIncomeNoteInfo(incomeNoteInfo: IncomeNoteInfo){
        databaseController.insertIncomeNoteData(incomeNoteInfo)
    }

    fun updateIncomeNoteInfo(incomeNoteInfo: IncomeNoteInfo){
        databaseController.updateIncomeNoteData(incomeNoteInfo)
    }

    fun deleteIncomeNoteInfo(id: Int){
        databaseController.deleteData(id, Databases.TABLE_INCOME_NOTE)

    }

    fun getAllIncomeNoteInfoList(): ArrayList<IncomeNoteInfo?>{
        return databaseController.getAllIncomeNoteList()
    }

    fun getGainIncomeNoteInfoList(): ArrayList<IncomeNoteInfo?>{
        return databaseController.getGainIncomeNoteList()
    }

    fun getLossIncomeNoteInfoList(): ArrayList<IncomeNoteInfo?>{
        return databaseController.getLossIncomeNoteList()
    }

    fun getIncomeNoteInfo(position: Int): IncomeNoteInfo?{
        val id = getAllIncomeNoteInfoList()[position]!!.id
        return databaseController.getIncomeNoteLInfo(id)
    }
}