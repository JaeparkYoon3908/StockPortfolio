package com.yjpapp.stockportfolio.repository

import android.content.Context
import androidx.paging.*
import com.yjpapp.stockportfolio.common.StockConfig
import com.yjpapp.stockportfolio.localdb.preference.PrefKey
import com.yjpapp.stockportfolio.model.request.ReqIncomeNoteInfo
import com.yjpapp.stockportfolio.model.response.RespIncomeNoteListInfo
import com.yjpapp.stockportfolio.model.response.RespStatusInfo
import com.yjpapp.stockportfolio.model.response.RespTotalGainIncomeNoteData
import com.yjpapp.stockportfolio.network.RetrofitClient
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

/**
 * IncomeNoteFragment의 Model 역할하는 class
 *
 * @author Yoon Jae-park
 * @since 2020.12
 */

class IncomeNoteRepository(
    private val preferenceRepository: PreferenceRepository,
    private val retrofitClient: RetrofitClient
) {
    suspend fun requestPostIncomeNote(reqIncomeNoteInfo: ReqIncomeNoteInfo): Response<RespIncomeNoteListInfo.IncomeNoteInfo>? {
        return retrofitClient.getService(RetrofitClient.BaseServerURL.RaspberryPi)?.requestPostIncomeNote(reqIncomeNoteInfo)
    }

    suspend fun requestDeleteIncomeNote(id: Int): Response<RespStatusInfo>? {
        return retrofitClient.getService(RetrofitClient.BaseServerURL.RaspberryPi)?.requestDeleteIncomeNote(id)
    }

    suspend fun requestPutIncomeNote(reqIncomeNoteInfo: ReqIncomeNoteInfo): Response<RespIncomeNoteListInfo.IncomeNoteInfo>? {
        return retrofitClient.getService(RetrofitClient.BaseServerURL.RaspberryPi)?.requestPutIncomeNote(reqIncomeNoteInfo)
    }

    suspend fun requestGetIncomeNote(params: HashMap<String, String>) : Response<RespIncomeNoteListInfo>? {
        return retrofitClient.getService(RetrofitClient.BaseServerURL.RaspberryPi)?.requestGetIncomeNote(params)
    }

    suspend fun requestTotalGain(params: HashMap<String, String>): Response<RespTotalGainIncomeNoteData>? {
        return retrofitClient.getService(RetrofitClient.BaseServerURL.RaspberryPi)?.requestTotalGainIncomeNote(params)
    }

    inner class IncomeNotePagingSource(var startDate: String, var endDate: String): PagingSource<Int, RespIncomeNoteListInfo.IncomeNoteInfo>() {
        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RespIncomeNoteListInfo.IncomeNoteInfo> {
            val page = params.key ?: 1
            return try {
                val hashMap = HashMap<String, String>()
                hashMap["page"] = page.toString()
                hashMap["size"] = params.loadSize.toString()
                hashMap["startDate"] = startDate
                hashMap["endDate"] = endDate
                val data = requestGetIncomeNote(hashMap)
                LoadResult.Page(
                    data = data?.body()?.income_note!!,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (data.body()?.income_note?.isEmpty()!!) null else page + 1
                )
            } catch (e: Exception) {
                return LoadResult.Error(e)
            }
        }

        override fun getRefreshKey(state: PagingState<Int, RespIncomeNoteListInfo.IncomeNoteInfo>): Int? {
            return state.anchorPosition?.let { anchorPosition ->
                state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                    ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
            }
        }
    }

    var currentPagingSource: IncomeNotePagingSource? = null
    fun getIncomeNoteListByPaging(startDate: String, endDate: String): Flow<PagingData<RespIncomeNoteListInfo.IncomeNoteInfo>> {
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = { IncomeNotePagingSource(startDate, endDate).also {
                currentPagingSource = it
            } }).flow
    }
    fun isShowDeleteCheck(): String {
        return preferenceRepository.getPreference(
            PrefKey.KEY_SETTING_INCOME_NOTE_SHOW_DELETE_CHECK
        )?: StockConfig.TRUE
    }
}
