package com.yjpapp.data.repository

import com.yjpapp.data.StockConfig
import com.yjpapp.data.datasource.IncomeNoteDataSource
import com.yjpapp.data.datasource.PreferenceDataSource
import com.yjpapp.data.localdb.preference.PrefKey
import com.yjpapp.data.model.request.ReqIncomeNoteInfo
import javax.inject.Inject

/**
 * 수익 노트 관련 Repository
 */
class IncomeNoteRepository @Inject constructor(
    private val incomeNoteDataSource: IncomeNoteDataSource,
    private val preferenceDataSource: PreferenceDataSource
) : BaseRepository() {
    suspend fun getIncomeNote(params: HashMap<String, String>) =
        incomeNoteDataSource.requestGetIncomeNote(params)

    suspend fun addIncomeNote(reqIncomeNoteInfo: ReqIncomeNoteInfo) =
        incomeNoteDataSource.requestPostIncomeNote(reqIncomeNoteInfo)

    suspend fun modifyIncomeNote(reqIncomeNoteInfo: ReqIncomeNoteInfo) =
        incomeNoteDataSource.requestPutIncomeNote(reqIncomeNoteInfo)

    suspend fun deleteIncomeNote(id: Int) =
        incomeNoteDataSource.requestDeleteIncomeNote(id)

    suspend fun getTotalGain(params: HashMap<String, String>) =
        incomeNoteDataSource.requestTotalGain(params)

    fun isShowDeleteCheck(): String {
        return preferenceDataSource.getPreference(
            PrefKey.KEY_SETTING_INCOME_NOTE_SHOW_DELETE_CHECK
        ) ?: StockConfig.TRUE
    }
}