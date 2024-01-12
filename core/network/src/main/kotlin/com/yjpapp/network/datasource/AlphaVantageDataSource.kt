package com.yjpapp.network.datasource

import com.yjpapp.network.model.RespUsaStockInfo
import com.yjpapp.network.model.RespUsaStockSymbolSearch
import retrofit2.Response

interface AlphaVantageDataSource {
    suspend fun getUSAStockSymbol(keywords: String): Response<RespUsaStockSymbolSearch>

    suspend fun getUSAStockInfo(symbol: String): Response<RespUsaStockInfo>
}