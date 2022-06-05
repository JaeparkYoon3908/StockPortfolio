package com.yjpapp.data.datasource

import com.yjpapp.data.model.request.ReqIncomeNoteInfo
import com.yjpapp.data.network.service.RaspberryPiService

/**
 * IncomeNoteFragment의 Model 역할하는 class
 *
 * @author Yoon Jae-park
 * @since 2020.12
 */

class IncomeNoteDataSource(
    private val raspberryPiAPIService: RaspberryPiService?
) {
    suspend fun requestPostIncomeNote(reqIncomeNoteInfo: ReqIncomeNoteInfo) =
        raspberryPiAPIService?.requestPostIncomeNote(reqIncomeNoteInfo)

    suspend fun requestDeleteIncomeNote(id: Int) =
        raspberryPiAPIService?.requestDeleteIncomeNote(id)

    suspend fun requestPutIncomeNote(reqIncomeNoteInfo: ReqIncomeNoteInfo) =
        raspberryPiAPIService?.requestPutIncomeNote(reqIncomeNoteInfo)

    suspend fun requestGetIncomeNote(params: HashMap<String, String>) =
        raspberryPiAPIService?.requestGetIncomeNote(params)

    suspend fun requestTotalGain(params: HashMap<String, String>) =
        raspberryPiAPIService?.requestTotalGainIncomeNote(params)

}
