package com.yjpapp.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.yjpapp.network.datasource.DataPortalDataSource
import com.yjpapp.network.model.RespStockPriceInfo
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.QueryMap
import javax.inject.Inject

private const val dataPortalBaseUrl = "https://apis.data.go.kr"

interface DataPortalApi {
    @GET("/1160100/service/GetStockSecuritiesInfoService/getStockPriceInfo")
    suspend fun getStockPriceInfo(@QueryMap params: HashMap<String, String>): Response<RespStockPriceInfo>
}
class RetrofitDataPortal @Inject constructor(
    networkJson: Json,
    okhttpCallFactory: Call.Factory,
): DataPortalDataSource {
    private val dataPortalApi = Retrofit.Builder()
        .baseUrl(dataPortalBaseUrl)
        .callFactory(okhttpCallFactory)
        .addConverterFactory(
            networkJson.asConverterFactory("application/json".toMediaType()),
        )
        .build()
        .create(DataPortalApi::class.java)

    override suspend fun getStockPriceInfo(params: HashMap<String, String>): Response<RespStockPriceInfo> =
        dataPortalApi.getStockPriceInfo(params)
}