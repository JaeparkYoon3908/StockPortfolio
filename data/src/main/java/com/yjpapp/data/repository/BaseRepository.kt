package com.yjpapp.data.repository

import com.yjpapp.data.model.RepositoryResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class BaseRepository {
    suspend fun <T> safeApiCall(dispatcher: CoroutineDispatcher = Dispatchers.IO, apiCall: suspend () -> T): RepositoryResult<T> {
        return withContext(dispatcher) {
            try {
                var result = apiCall.invoke()
                RepositoryResult.Success(result, "200", "성공")
            } catch (throwable: Exception) {
                // show toast message or alert which error message
                // call same api again apiCall.invoke() or safeApiCall()
                RepositoryResult.DataError("500",throwable.message?: "")
            }
        }
    }
}