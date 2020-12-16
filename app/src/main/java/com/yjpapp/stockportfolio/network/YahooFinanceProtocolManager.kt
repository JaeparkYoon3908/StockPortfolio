package com.yjpapp.stockportfolio.network

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.yjpapp.stockportfolio.constance.AppConfig
import com.yjpapp.stockportfolio.network.model.Price
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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

    fun getStockProfile(){
        addHeaderMap()
        val queryMap:HashMap<String,String> = HashMap()
        queryMap["symbol"] = "005930.KS"
        queryMap["region"] = "KR"
//        val request = Request("005930.KS", "KR")
        YahooFinanceClient.ourInstance.service.getMyJsonObject(headerMap, queryMap)?.enqueue(object: Callback<JsonObject?> {
            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                if(response.code() == 200 || response.code() == 204){
                    try {
//                    val formattedResult = StringBuilder()
                        val jsonObject = JSONObject(response.body().toString())
                        val responseJSONArray = jsonObject.getJSONObject("price").toString()
                        val price: Price = Gson().fromJson(responseJSONArray, Price::class.java)
                        Log.d("YJP", "price = $price")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                Log.d("RequestResult", "RetrofitExample, Type : get, Result : onFailure, Error Message : " + t.message)
            }
        })

//        res?.enqueue(object: Callback<JsonObject?> {
//            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
//                try {
////                    val formattedResult = StringBuilder()
//                    val jsonObject = JSONObject(response.body().toString())
//                    val responseJSONArray = jsonObject.getJSONArray("price")
//                    Log.d("YJP", responseJSONArray.toString())
//
//                } catch (e: JSONException) {
//                    e.printStackTrace()
//                }
//            }
//
//            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
//                Log.d("RequestResult", "RetrofitExample, Type : get, Result : onFailure, Error Message : " + t.message)
//            }
//        })

    }
}