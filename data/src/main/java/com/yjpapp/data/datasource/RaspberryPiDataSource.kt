package com.yjpapp.data.datasource

import com.yjpapp.data.APICall
import com.yjpapp.data.model.ResponseResult
import com.yjpapp.data.model.request.ReqIncomeNoteInfo
import com.yjpapp.data.model.response.RespIncomeNoteListInfo
import com.yjpapp.data.model.response.RespStatusInfo
import com.yjpapp.data.model.response.RespTotalGainIncomeNoteData
import com.yjpapp.data.network.service.RaspberryPiService
import javax.inject.Inject

/**
 * 라즈베리파이 서버 전용 DataSource
 */
class RaspberryPiDataSource @Inject constructor(
    private val raspberryPiService: RaspberryPiService
) {
    suspend fun requestPostIncomeNote(reqIncomeNoteInfo: ReqIncomeNoteInfo): ResponseResult<RespIncomeNoteListInfo.IncomeNoteInfo> =
        APICall.handleApi { raspberryPiService.requestPostIncomeNote(reqIncomeNoteInfo) }

    suspend fun requestDeleteIncomeNote(id: Int): ResponseResult<RespStatusInfo> =
        APICall.handleApi { raspberryPiService.requestDeleteIncomeNote(id) }

    suspend fun requestPutIncomeNote(reqIncomeNoteInfo: ReqIncomeNoteInfo): ResponseResult<RespIncomeNoteListInfo.IncomeNoteInfo> =
        APICall.handleApi { raspberryPiService.requestPutIncomeNote(reqIncomeNoteInfo) }

    suspend fun requestGetIncomeNote(params: HashMap<String, String>): ResponseResult<RespIncomeNoteListInfo> =
        APICall.handleApi { raspberryPiService.requestGetIncomeNote(params)  }

    suspend fun requestTotalGain(params: HashMap<String, String>): ResponseResult<RespTotalGainIncomeNoteData> =
        APICall.handleApi { raspberryPiService.requestTotalGainIncomeNote(params) }
}