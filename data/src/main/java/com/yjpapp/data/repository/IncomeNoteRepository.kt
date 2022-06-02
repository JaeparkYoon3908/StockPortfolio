package com.yjpapp.data.repository

import com.yjpapp.data.StockConfig
import com.yjpapp.data.datasource.IncomeNoteDataSource
import com.yjpapp.data.datasource.PreferenceDataSource
import com.yjpapp.data.localdb.preference.PrefKey
import com.yjpapp.data.model.request.ReqIncomeNoteInfo
import com.yjpapp.data.network.RetrofitClient
import javax.inject.Inject

/**
 * 수익 노트 관련 Repository
 */
class IncomeNoteRepository @Inject constructor(
    private val incomeNoteDataSource: IncomeNoteDataSource,
    private val preferenceDataSource: PreferenceDataSource
) : BaseRepository() {
    suspend fun getIncomeNote(params: HashMap<String, String>) =
        safeApiCall { incomeNoteDataSource.requestGetIncomeNote(params)?.body() }

    suspend fun addIncomeNote(reqIncomeNoteInfo: ReqIncomeNoteInfo) =
        safeApiCall { incomeNoteDataSource.requestPostIncomeNote(reqIncomeNoteInfo)?.body() }

    suspend fun modifyIncomeNote(reqIncomeNoteInfo: ReqIncomeNoteInfo) =
        safeApiCall { incomeNoteDataSource.requestPutIncomeNote(reqIncomeNoteInfo)?.body() }

    suspend fun deleteIncomeNote(id: Int) =
        safeApiCall { incomeNoteDataSource.requestDeleteIncomeNote(id)?.body() }

    suspend fun getTotalGain(params: HashMap<String, String>) =
        safeApiCall { incomeNoteDataSource.requestTotalGain(params)?.body() }

    fun isShowDeleteCheck(): String {
        return preferenceDataSource.getPreference(
            PrefKey.KEY_SETTING_INCOME_NOTE_SHOW_DELETE_CHECK
        ) ?: StockConfig.TRUE
    }
}