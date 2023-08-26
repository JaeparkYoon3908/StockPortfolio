package com.yjpapp.stockportfolio.data.network.service

import com.yjpapp.stockportfolio.data.model.response.RespStockPriceInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface DataPortalService {
    @GET("/1160100/service/GetStockSecuritiesInfoService/getStockPriceInfo")
    suspend fun getStockPriceInfo(@QueryMap params: HashMap<String, String>): Response<RespStockPriceInfo>
}