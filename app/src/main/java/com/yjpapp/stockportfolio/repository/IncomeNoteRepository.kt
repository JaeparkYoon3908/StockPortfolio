package com.yjpapp.stockportfolio.repository

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yjpapp.stockportfolio.model.IncomeNoteModel
import com.yjpapp.stockportfolio.network.RetrofitClient

class IncomeNoteRepository {

    suspend fun requestPostIncomeNote(context: Context, incomeNoteModel: IncomeNoteModel) =
        RetrofitClient.getService(context, RetrofitClient.BaseServerURL.MY)?.requestPostIncomeNote(incomeNoteModel)

    suspend fun requestDeleteIncomeNote(context: Context, id: Int) =
        RetrofitClient.getService(context, RetrofitClient.BaseServerURL.MY)?.requestDeleteIncomeNote(id)


    suspend fun requestPutIncomeNote(context: Context, incomeNoteModel: IncomeNoteModel) =
        RetrofitClient.getService(context, RetrofitClient.BaseServerURL.MY)?.requestPutIncomeNote(incomeNoteModel)

    suspend fun requestGetIncomeNote(context: Context, params: HashMap<String, String>) =
        RetrofitClient.getService(context, RetrofitClient.BaseServerURL.MY)?.requestGetIncomeNote(params)

    inner class IncomeNotePagingSource(val context: Context): PagingSource<Int, IncomeNoteModel>() {
        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, IncomeNoteModel> {
            val page = params.key ?: 1
            return try {
                //TODO 코드 개선하기.
                val hashMap = HashMap<String, String>()
                hashMap["page"] = page.toString()
                hashMap["size"] = "20"
                val data = requestGetIncomeNote(context, hashMap)
                LoadResult.Page(
                    data = data?.body()!!,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (data.body()?.isEmpty()!!) null else page + (params.loadSize / 10)
                )
            } catch (e: Exception) {
                return LoadResult.Error(e)
            }
        }

        override fun getRefreshKey(state: PagingState<Int, IncomeNoteModel>): Int? {
            return state.anchorPosition?.let { anchorPosition ->
                state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                    ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
            }
        }
    }
}