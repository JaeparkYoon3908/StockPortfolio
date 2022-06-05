package com.yjpapp.data.repository

import com.yjpapp.data.model.RepositoryResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class BaseRepository {
    suspend fun <T> safeApiCall(dispatcher: CoroutineDispatcher = Dispatchers.IO, apiCall: suspend () -> T): RepositoryResult<T> {
        return withContext(dispatcher) {
            try {
                val result = apiCall.invoke()
                RepositoryResult.Success(result, "200", "성공")
            } catch (throwable: Exception) {
                RepositoryResult.DataError("500",throwable.message?: "")
            }
        }
    }
}