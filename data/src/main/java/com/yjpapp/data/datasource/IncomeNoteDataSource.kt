package com.yjpapp.data.datasource

import androidx.paging.*
import com.yjpapp.data.StockConfig
import com.yjpapp.data.localdb.preference.PrefKey
import com.yjpapp.data.model.RepositoryResult
import com.yjpapp.data.model.request.ReqIncomeNoteInfo
import com.yjpapp.data.model.response.RespIncomeNoteListInfo
import com.yjpapp.data.model.response.RespStatusInfo
import com.yjpapp.data.model.response.RespTotalGainIncomeNoteData
import com.yjpapp.data.network.RetrofitClient
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

/**
 * IncomeNoteFragment의 Model 역할하는 class
 *
 * @author Yoon Jae-park
 * @since 2020.12
 */

class IncomeNoteDataSource(
    private val retrofitClient: RetrofitClient
) {
    suspend fun requestPostIncomeNote(reqIncomeNoteInfo: ReqIncomeNoteInfo) =
        retrofitClient.getService(RetrofitClient.BaseServerURL.RaspberryPi)?.requestPostIncomeNote(reqIncomeNoteInfo)

    suspend fun requestDeleteIncomeNote(id: Int) =
        retrofitClient.getService(RetrofitClient.BaseServerURL.RaspberryPi)?.requestDeleteIncomeNote(id)

    suspend fun requestPutIncomeNote(reqIncomeNoteInfo: ReqIncomeNoteInfo) =
        retrofitClient.getService(RetrofitClient.BaseServerURL.RaspberryPi)?.requestPutIncomeNote(reqIncomeNoteInfo)

    suspend fun requestGetIncomeNote(params: HashMap<String, String>) =
        retrofitClient.getService(RetrofitClient.BaseServerURL.RaspberryPi)?.requestGetIncomeNote(params)

    suspend fun requestTotalGain(params: HashMap<String, String>) =
        retrofitClient.getService(RetrofitClient.BaseServerURL.RaspberryPi)?.requestTotalGainIncomeNote(params)

}
