package com.yjpapp.data.datasource

import com.yjpapp.data.APICall
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
import javax.inject.Inject

/**
 * IncomeNoteFragment의 Model 역할하는 class
 *
 * @author Yoon Jae-park
 * @since 2020.12
 */

class IncomeNoteDataSource @Inject constructor(
    private val raspberryPiAPIService: RaspberryPiService
) {
    suspend fun requestPostIncomeNote(reqIncomeNoteInfo: ReqIncomeNoteInfo): ResponseResult<RespIncomeNoteListInfo.IncomeNoteInfo> =
        APICall.handleApi { raspberryPiAPIService.requestPostIncomeNote(reqIncomeNoteInfo) }

    suspend fun requestDeleteIncomeNote(id: Int): ResponseResult<RespStatusInfo> =
        APICall.handleApi { raspberryPiAPIService.requestDeleteIncomeNote(id) }

    suspend fun requestPutIncomeNote(reqIncomeNoteInfo: ReqIncomeNoteInfo): ResponseResult<RespIncomeNoteListInfo.IncomeNoteInfo> =
        APICall.handleApi { raspberryPiAPIService.requestPutIncomeNote(reqIncomeNoteInfo) }

    suspend fun requestGetIncomeNote(params: HashMap<String, String>): ResponseResult<RespIncomeNoteListInfo> =
        APICall.handleApi { raspberryPiAPIService.requestGetIncomeNote(params)  }

    suspend fun requestTotalGain(params: HashMap<String, String>): ResponseResult<RespTotalGainIncomeNoteData> =
        APICall.handleApi { raspberryPiAPIService.requestTotalGainIncomeNote(params) }
}

