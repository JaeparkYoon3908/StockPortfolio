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

/**
 * IncomeNoteFragment의 Model 역할하는 class
 *
 * @author Yoon Jae-park
 * @since 2020.12
 */

class IncomeNoteDataSource(
    private val raspberryPiAPIService: RaspberryPiService
): BaseDataSource() {
    suspend fun requestPostIncomeNote(reqIncomeNoteInfo: ReqIncomeNoteInfo): ResponseResult<RespIncomeNoteListInfo.IncomeNoteInfo> =
        handleApi { raspberryPiAPIService.requestPostIncomeNote(reqIncomeNoteInfo) }

    suspend fun requestDeleteIncomeNote(id: Int): ResponseResult<RespStatusInfo> =
        handleApi { raspberryPiAPIService.requestDeleteIncomeNote(id) }

    suspend fun requestPutIncomeNote(reqIncomeNoteInfo: ReqIncomeNoteInfo): ResponseResult<RespIncomeNoteListInfo.IncomeNoteInfo> =
        handleApi { raspberryPiAPIService.requestPutIncomeNote(reqIncomeNoteInfo) }

    suspend fun requestGetIncomeNote(params: HashMap<String, String>): ResponseResult<RespIncomeNoteListInfo> =
        handleApi { raspberryPiAPIService.requestGetIncomeNote(params)  }

    suspend fun requestTotalGain(params: HashMap<String, String>): ResponseResult<RespTotalGainIncomeNoteData> =
        handleApi { raspberryPiAPIService.requestTotalGainIncomeNote(params) }
}

