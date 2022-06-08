package com.yjpapp.data.datasource

import com.yjpapp.data.model.ResponseResult
import com.yjpapp.data.model.request.ReqIncomeNoteInfo
import com.yjpapp.data.model.response.RespIncomeNoteListInfo
import com.yjpapp.data.model.response.RespStatusInfo
import com.yjpapp.data.model.response.RespTotalGainIncomeNoteData
import com.yjpapp.data.network.ServerRespCode
import com.yjpapp.data.network.ServerRespMessage
import com.yjpapp.data.network.service.RaspberryPiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

/**
 * IncomeNoteFragment의 Model 역할하는 class
 *
 * @author Yoon Jae-park
 * @since 2020.12
 */

class IncomeNoteDataSource(
    private val raspberryPiAPIService: RaspberryPiService?
) {
    private val ioDispatcher = Dispatchers.IO
    suspend fun requestPostIncomeNote(reqIncomeNoteInfo: ReqIncomeNoteInfo): ResponseResult<RespIncomeNoteListInfo.IncomeNoteInfo?> =
        withContext(ioDispatcher) {
            try {
                val result = raspberryPiAPIService?.requestPostIncomeNote(reqIncomeNoteInfo)
                if (result?.isSuccessful == true) {
                    ResponseResult.Success(result.body(), ServerRespCode.OK, ServerRespMessage.Success)
                } else {
                    ResponseResult.DataError(ServerRespCode.BadRequest, ServerRespMessage.Fail)
                }
            } catch (e: Exception) {
                ResponseResult.DataError(ServerRespCode.Exception, "${e.message}")
            }
        }

    suspend fun requestDeleteIncomeNote(id: Int): ResponseResult<RespStatusInfo?> =
        withContext(ioDispatcher) {
            try {
                val result = raspberryPiAPIService?.requestDeleteIncomeNote(id)
                if (result?.isSuccessful == true) {
                    ResponseResult.Success(result.body(), ServerRespCode.OK, ServerRespMessage.Success)
                } else {
                    ResponseResult.DataError(ServerRespCode.BadRequest, ServerRespMessage.Fail)
                }
            } catch (e: Exception) {
                ResponseResult.DataError(ServerRespCode.Exception, "${e.message}")
            }
        }

    suspend fun requestPutIncomeNote(reqIncomeNoteInfo: ReqIncomeNoteInfo): ResponseResult<RespIncomeNoteListInfo.IncomeNoteInfo?> =
        withContext(ioDispatcher) {
            try {
                val result = raspberryPiAPIService?.requestPutIncomeNote(reqIncomeNoteInfo)
                if (result?.isSuccessful == true) {
                    ResponseResult.Success(result.body(), ServerRespCode.OK, ServerRespMessage.Success)
                } else {
                    ResponseResult.DataError(ServerRespCode.BadRequest, ServerRespMessage.Fail)
                }
            } catch (e: Exception) {
                ResponseResult.DataError(ServerRespCode.Exception, "${e.message}")
            }
        }

    suspend fun requestGetIncomeNote(params: HashMap<String, String>): ResponseResult<RespIncomeNoteListInfo?> =
        withContext(ioDispatcher) {
            try {
                val result = raspberryPiAPIService?.requestGetIncomeNote(params)
                if (result?.isSuccessful == true) {
                    ResponseResult.Success(result.body(), ServerRespCode.OK, ServerRespMessage.Success)
                } else {
                    ResponseResult.DataError(ServerRespCode.BadRequest, ServerRespMessage.Fail)
                }
            } catch (e: Exception) {
                ResponseResult.DataError(ServerRespCode.Exception, "${e.message}")
            }
        }

    suspend fun requestTotalGain(params: HashMap<String, String>): ResponseResult<RespTotalGainIncomeNoteData?> =
        withContext(ioDispatcher) {
            try {
                val result = raspberryPiAPIService?.requestTotalGainIncomeNote(params)
                if (result?.isSuccessful == true) {
                    ResponseResult.Success(result.body(), ServerRespCode.OK, ServerRespMessage.Success)
                } else {
                    ResponseResult.DataError(ServerRespCode.BadRequest, ServerRespMessage.Fail)
                }
            } catch (e: Exception) {
                ResponseResult.DataError(ServerRespCode.Exception, "${e.message}")
            }
        }
}

