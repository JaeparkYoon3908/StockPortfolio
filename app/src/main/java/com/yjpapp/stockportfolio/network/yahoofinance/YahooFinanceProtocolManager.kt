package com.yjpapp.stockportfolio.network.yahoofinance

import android.content.Context
import com.google.gson.JsonObject
import com.yjpapp.stockportfolio.constance.AppConfig
import retrofit2.Callback
import java.util.*

class YahooFinanceProtocolManager {
    private val KEY_X_RAPIDAPI = "x-rapidapi-key"
    private val KEY_X_RAPIDAPI_HOST = "x-rapidapi-host"

    companion object {
        @Volatile private var instance: YahooFinanceProtocolManager? = null
        private lateinit var mContext: Context
        private lateinit var headerMap: HashMap<String,String>

        @JvmStatic
        fun getInstance(context: Context): YahooFinanceProtocolManager =
            instance ?: synchronized(this) {
                instance ?: YahooFinanceProtocolManager().also {
                    instance = it
                    mContext = context
                    headerMap = HashMap()
                 }
            }
    }

    private fun addHeaderMap(){
        headerMap[KEY_X_RAPIDAPI] = AppConfig.x_rapidapi_key
        headerMap[KEY_X_RAPIDAPI_HOST] = AppConfig.x_rapidapi_host
    }

    fun getStockProfile(symbol: String, region: String, callback: Callback<JsonObject?>){
        addHeaderMap()
        val queryMap:HashMap<String,String> = HashMap()
        queryMap["symbol"] = symbol
        queryMap["region"] = region

        YahooFinanceClient.ourInstance.service.getMyJsonObject(headerMap, queryMap)?.enqueue(callback)

    }
}