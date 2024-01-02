package com.yjpapp.data.repository

import com.yjpapp.data.datasource.PreferenceDataSource
import com.yjpapp.data.model.ResponseResult
import javax.inject.Inject

interface MySettingRepository {
    suspend fun setDefaultMyStockTitle(title: String): ResponseResult<Boolean>
    suspend fun getDefaultMyStockTitle(): ResponseResult<String>

    suspend fun setIsFirstAppRun(value: Boolean): ResponseResult<Boolean>
    suspend fun getIsFirstAppRun(): ResponseResult<Boolean>
}

class MySettingRepositoryImpl @Inject constructor(
    private val pref: PreferenceDataSource
) : MySettingRepository {
    private val defaultMyStockTitleKey = "DefaultMyStockTitle"
    private val isFirstAppRunKey = "isFirstAppRun"
    override suspend fun setDefaultMyStockTitle(title: String): ResponseResult<Boolean> {
        return try {
            pref.setPreference(defaultMyStockTitleKey, title)
            ResponseResult.Success(data = true, resultCode = "200", resultMessage = "OK")
        } catch (e: Exception) {
            ResponseResult.Error(data = false, errorCode = "500", errorMessage = e.message?: "Unknown error")
        }
    }

    override suspend fun getDefaultMyStockTitle(): ResponseResult<String> {
        return try {
            val data = pref.getPreference(defaultMyStockTitleKey)
            ResponseResult.Success(data = data, resultCode = "200", resultMessage = "OK")
        } catch (e: Exception) {
            ResponseResult.Error(errorCode = "500", errorMessage = e.message?: "Unknown error")
        }
    }

    override suspend fun setIsFirstAppRun(value: Boolean): ResponseResult<Boolean> {
        return try {
            pref.setPreference(isFirstAppRunKey, value)
            ResponseResult.Success(data = true, resultCode = "200", resultMessage = "OK")
        } catch (e: Exception) {
            ResponseResult.Error(data = false, errorCode = "500", errorMessage = e.message?: "Unknown error")
        }
    }

    override suspend fun getIsFirstAppRun(): ResponseResult<Boolean> {
        return try {
            ResponseResult.Success(data = pref.getPreference(isFirstAppRunKey) == "true", resultCode = "200", resultMessage = "OK")
        } catch (e: Exception) {
            ResponseResult.Error(data = false, errorCode = "500", errorMessage = e.message?: "Unknown error")
        }
    }

}