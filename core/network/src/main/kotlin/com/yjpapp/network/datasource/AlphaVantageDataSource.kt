package com.yjpapp.network.datasource

import com.yjpapp.network.model.RespUsaStockSymbolSearch
import retrofit2.Response

interface AlphaVantageDataSource {
    suspend fun getUSAStockSymbol(keywords: String): Response<RespUsaStockSymbolSearch>
}