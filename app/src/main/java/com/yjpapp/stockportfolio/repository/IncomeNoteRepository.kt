package com.yjpapp.stockportfolio.repository

import android.content.Context
import androidx.paging.*
import com.yjpapp.stockportfolio.model.request.ReqIncomeNoteInfo
import com.yjpapp.stockportfolio.model.response.RespIncomeNoteListInfo
import com.yjpapp.stockportfolio.network.RetrofitClient
import kotlinx.coroutines.flow.Flow

/**
 * IncomeNoteFragment의 Model 역할하는 class
 *
 * @author Yoon Jae-park
 * @since 2020.12
 */
class IncomeNoteRepository  {
    suspend fun requestPostIncomeNote(context: Context, reqIncomeNoteInfo: ReqIncomeNoteInfo) =
        RetrofitClient.getService(context, RetrofitClient.BaseServerURL.MY)?.requestPostIncomeNote(reqIncomeNoteInfo)

    suspend fun requestDeleteIncomeNote(context: Context, id: Int) =
        RetrofitClient.getService(context, RetrofitClient.BaseServerURL.MY)?.requestDeleteIncomeNote(id)

    suspend fun requestPutIncomeNote(context: Context, reqIncomeNoteInfo: ReqIncomeNoteInfo) =
        RetrofitClient.getService(context, RetrofitClient.BaseServerURL.MY)?.requestPutIncomeNote(reqIncomeNoteInfo)

    suspend fun requestGetIncomeNote(context: Context, params: HashMap<String, String>) =
        RetrofitClient.getService(context, RetrofitClient.BaseServerURL.MY)?.requestGetIncomeNote(params)

    suspend fun requestTotalGain(context: Context, params: HashMap<String, String>) =
        RetrofitClient.getService(context, RetrofitClient.BaseServerURL.MY)?.requestTotalGainIncomeNote(params)

    inner class IncomeNotePagingSource(val context: Context, var startDate: String, var endDate: String): PagingSource<Int, RespIncomeNoteListInfo.IncomeNoteInfo>() {
        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RespIncomeNoteListInfo.IncomeNoteInfo> {
            val page = params.key ?: 1
            return try {
                val hashMap = HashMap<String, String>()
                hashMap["page"] = page.toString()
                hashMap["size"] = params.loadSize.toString()
                hashMap["startDate"] = startDate
                hashMap["endDate"] = endDate
                val data = requestGetIncomeNote(context, hashMap)
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
    fun getIncomeNoteListByPaging(context: Context, startDate: String, endDate: String): Flow<PagingData<RespIncomeNoteListInfo.IncomeNoteInfo>> {
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = { IncomeNotePagingSource(context, startDate, endDate).also {
                currentPagingSource = it
            } }).flow
    }

}