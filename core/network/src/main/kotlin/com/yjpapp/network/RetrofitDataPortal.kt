package com.yjpapp.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.yjpapp.network.datasource.AlphaVantageDataSource
import com.yjpapp.network.datasource.DataPortalDataSource
import com.yjpapp.network.model.RespStockPriceInfo
import com.yjpapp.network.model.RespUsaStockSymbolSearch
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap
import javax.inject.Inject

private const val dataPortalBaseUrl = "https://apis.data.go.kr"
private const val alphaVantageBaseUrl = "https://www.alphavantage.co"
interface DataPortalApi {
    @GET("/1160100/service/GetStockSecuritiesInfoService/getStockPriceInfo")
    suspend fun getStockPriceInfo(
        @QueryMap params: HashMap<String, String>
    ): Response<RespStockPriceInfo>
}
interface AlphaVantageApi {
    @GET("/query")
    suspend fun getSymbolSearch(
        @Query("function")function: String = "SYMBOL_SEARCH",
        @Query("keywords")keywords: String,
        @Query("apikey")apikey: String = "6RTTL7DXZNIV1SXN",
    ): Response<RespUsaStockSymbolSearch>
}
class RetrofitDataPortal @Inject constructor(
    networkJson: Json,
    okhttpCallFactory: Call.Factory,
): DataPortalDataSource, AlphaVantageDataSource {
    private val dataPortalApi = Retrofit.Builder()
        .baseUrl(dataPortalBaseUrl)
        .callFactory(okhttpCallFactory)
        .addConverterFactory(
            networkJson.asConverterFactory("application/json".toMediaType()),
        )
        .build()
        .create(DataPortalApi::class.java)

    private val alphaVantageApi = Retrofit.Builder()
        .baseUrl(alphaVantageBaseUrl)
        .callFactory(okhttpCallFactory)
        .addConverterFactory(
            networkJson.asConverterFactory("application/json".toMediaType()),
        )
        .build()
        .create(AlphaVantageApi::class.java)

    override suspend fun getKoreaStockPriceInfo(params: HashMap<String, String>): Response<RespStockPriceInfo> =
        dataPortalApi.getStockPriceInfo(params)

    override suspend fun getUSAStockSymbol(keywords: String): Response<RespUsaStockSymbolSearch> =
        alphaVantageApi.getSymbolSearch(keywords = keywords)



}