package com.yjpapp.network

import com.yjpapp.network.model.RespStockPriceInfo
import retrofit2.Response
interface SPNetworkDataSource {
    suspend fun getStockPriceInfo(params: HashMap<String, String>): Response<RespStockPriceInfo>
}