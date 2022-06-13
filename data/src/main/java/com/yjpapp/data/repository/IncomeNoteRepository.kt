package com.yjpapp.data.repository

import com.yjpapp.data.StockConfig
import com.yjpapp.data.datasource.IncomeNoteDataSource
import com.yjpapp.data.datasource.PreferenceDataSource
import com.yjpapp.data.localdb.preference.PrefKey
import com.yjpapp.data.model.ResponseResult
import com.yjpapp.data.model.request.ReqIncomeNoteInfo
import com.yjpapp.data.model.response.RespIncomeNoteListInfo
import com.yjpapp.data.model.response.RespStatusInfo
import com.yjpapp.data.model.response.RespTotalGainIncomeNoteData
import javax.inject.Inject

/**
 * 수익 노트 관련 Repository
 */
interface IncomeNoteRepository {
    suspend fun getIncomeNote(params: HashMap<String, String>): ResponseResult<RespIncomeNoteListInfo>
    suspend fun addIncomeNote(reqIncomeNoteInfo: ReqIncomeNoteInfo): ResponseResult<RespIncomeNoteListInfo.IncomeNoteInfo>
    suspend fun modifyIncomeNote(reqIncomeNoteInfo: ReqIncomeNoteInfo): ResponseResult<RespIncomeNoteListInfo.IncomeNoteInfo>
    suspend fun deleteIncomeNote(id: Int): ResponseResult<RespStatusInfo>
    suspend fun getTotalGain(params: HashMap<String, String>): ResponseResult<RespTotalGainIncomeNoteData>
    fun isShowDeleteCheck(): String
}
class IncomeNoteRepositoryImpl @Inject constructor(
    private val incomeNoteDataSource: IncomeNoteDataSource,
    private val preferenceDataSource: PreferenceDataSource
): IncomeNoteRepository {
    override suspend fun getIncomeNote(params: HashMap<String, String>) =
        incomeNoteDataSource.requestGetIncomeNote(params)

    override suspend fun addIncomeNote(reqIncomeNoteInfo: ReqIncomeNoteInfo) =
        incomeNoteDataSource.requestPostIncomeNote(reqIncomeNoteInfo)

    override suspend fun modifyIncomeNote(reqIncomeNoteInfo: ReqIncomeNoteInfo) =
        incomeNoteDataSource.requestPutIncomeNote(reqIncomeNoteInfo)

    override suspend fun deleteIncomeNote(id: Int) =
        incomeNoteDataSource.requestDeleteIncomeNote(id)

    override suspend fun getTotalGain(params: HashMap<String, String>) =
        incomeNoteDataSource.requestTotalGain(params)

    override fun isShowDeleteCheck(): String {
        return preferenceDataSource.getPreference(
            PrefKey.KEY_SETTING_INCOME_NOTE_SHOW_DELETE_CHECK
        ) ?: StockConfig.TRUE
    }
}

