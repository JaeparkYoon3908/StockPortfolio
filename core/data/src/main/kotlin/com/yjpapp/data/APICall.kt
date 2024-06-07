package com.yjpapp.data

import com.yjpapp.data.model.ResponseResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response

/**
 * 공용 API Call 함수가 구현 된 base class
 */
object APICall {
    suspend fun <T : Any> requestApi(
        execute: suspend () -> Response<T>
    ): ResponseResult<T> {
        return try {
            val response = execute()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                ResponseResult.Success(body, "200", "SUCCESS")
            } else {
                ResponseResult.Error("400", "Fail")
            }
        } catch (e: HttpException) {
            ResponseResult.Error("500", "${e.message}")
        } catch (e: Throwable) {
            ResponseResult.Error("501", "${e.message}")
        }
    }
}