package com.yjpapp.stockportfolio.ui.interactor

import android.content.Context
import com.yjpapp.stockportfolio.database.DatabaseController
import com.yjpapp.stockportfolio.database.data.IncomeNoteInfo

class IncomeNoteInteractor {

    fun insertIncomeNoteInfo(mContext: Context, incomeNoteInfo: IncomeNoteInfo){
        DatabaseController.getInstance(mContext).insertIncomeNoteData(incomeNoteInfo)
    }

    fun updateIncomeNoteInfo(mContext: Context, incomeNoteInfo: IncomeNoteInfo){
        DatabaseController.getInstance(mContext).updateIncomeNoteData(incomeNoteInfo)
    }

    fun getAllIncomeNoteInfo(mContext: Context): ArrayList<IncomeNoteInfo?>{
        return DatabaseController.getInstance(mContext).getAllIncomeNoteList()
    }

    fun getGainIncomeNoteInfo(mContext: Context): ArrayList<IncomeNoteInfo?>{
        return DatabaseController.getInstance(mContext).getGainIncomeNoteList()
    }

    fun getLossIncomeNoteInfo(mContext: Context): ArrayList<IncomeNoteInfo?>{
        return DatabaseController.getInstance(mContext).getLossIncomeNoteList()
    }
}