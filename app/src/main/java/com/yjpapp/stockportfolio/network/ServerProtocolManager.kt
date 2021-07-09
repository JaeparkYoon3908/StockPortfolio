package com.yjpapp.stockportfolio.network

import com.google.gson.JsonObject
import com.yjpapp.stockportfolio.model.TestModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.coroutines.Continuation

/**
 * Repository로 이동 추천
 */
class ServerProtocolManager {
    companion object {
        @Volatile private var instance: ServerProtocolManager? = null
        private lateinit var headerMap: HashMap<String, String>

        @JvmStatic
        fun getInstance(): ServerProtocolManager =
            instance ?: synchronized(this) {
                instance ?: ServerProtocolManager().also {
                    instance = it
                    headerMap = HashMap()
                }
            }
    }
    private fun addHeaderMap(){

    }

    suspend fun getStockProfile(symbol: String, region: String, callback: Callback<JsonObject?>){
        addHeaderMap()
//        val queryMap: HashMap<String, String> = HashMap()
//        queryMap["symbol"] = symbol
//        queryMap["region"] = region
//
//        RetrofitClient.ourInstance.service.getMyJsonObject(headerMap, queryMap)?.enqueue(object : Callback<JsonObject?>{
//            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
//                TODO("Not yet implemented")
//            }
//
//        })
    }

}