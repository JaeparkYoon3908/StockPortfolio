package com.yjpapp.data.repository

import com.yjpapp.data.model.ResponseResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class BaseRepository {
    suspend fun <T> safeApiCall(dispatcher: CoroutineDispatcher = Dispatchers.IO, apiCall: suspend () -> T): ResponseResult<T> {
        return withContext(dispatcher) {
            try {
                val result = apiCall.invoke()
                ResponseResult.Success(result, "200", "성공")
            } catch (throwable: Exception) {
                ResponseResult.DataError("500",throwable.message?: "")
            }
        }
    }
}