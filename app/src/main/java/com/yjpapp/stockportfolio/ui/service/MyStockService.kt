package com.yjpapp.stockportfolio.ui.service

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.yjpapp.stockportfolio.network.YahooFinanceProtocolManager
import com.yjpapp.stockportfolio.network.data.Price
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * MyStockFragment와 연결된 Service
 * 백그라운드에서 현재 주가를 갖고오기 위해 만듬.
 *
 * @author Yun Jae-park
 * @since 2020.12
 */
class MyStockService: Service() {
    companion object {
        const val MSG_SEND_TO_ACTIVITY = 0
        const val MSG_SEND_TO_SERVICE = 1
        const val MSG_REGISTER_CLIENT = 2
        const val MSG_DATA_REQUEST = 3
        const val MSG_CLIENT_REGI_SUCCESS = 4
    }
    private lateinit var mClient: Messenger
    /** activity로부터 binding 된 Messenger  */
    private val mMessenger: Messenger = Messenger(Handler(Looper.getMainLooper()) { msg ->
        Log.w("test", "ControlService - message what : " + msg.what.toString() + " , msg.obj " + msg.obj)
        when (msg.what) {
            // activity로부터 가져옴
            MSG_REGISTER_CLIENT -> {
//                msg.data.get("presenter")
                mClient = msg.replyTo
                dataRequest()

            }
            MSG_DATA_REQUEST -> {
                dataRequest()
            }
        }
        false
    })

//    private val mMessenger: Messenger = Messenger(Handler(Looper.getMainLooper()) { msg ->
//        Log.w("test", "ControlService - message what : " + msg.what + " , msg.obj " + msg.obj)
//        when (msg.what) {
//            MSG_REGISTER_CLIENT -> {
//                mClient = msg.replyTo
//            }
//        }
//        false
//    })

    private fun sendMsgToActivity(sendValue: Int){
        try{
            val bundle = Bundle()
            bundle.putInt("fromService", sendValue)
            bundle.putString("test", "abcdefg")
            val msg = Message.obtain(null, MSG_SEND_TO_ACTIVITY)
            msg.data = bundle
            mClient.send(msg)
        }catch (e: RemoteException){

        }
    }

    override fun onCreate() {
        super.onCreate()

    }

    override fun onBind(intent: Intent?): IBinder? {
        // Service 객체와 (화면단 Activity 사이에서)
        // 통신(데이터를 주고받을) 때 사용하는 메서드
        // 데이터를 전달할 필요가 없으면 return null;

//        sendMsgToActivity(1234)

        return mMessenger.binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //서비스가 호출될 때마다 실행
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        //서비스가 종료될 때 실행.
    }

    private fun dataRequest(){
        val symbol = "005930.KS"
        val region = "KR"
        YahooFinanceProtocolManager.getInstance(applicationContext).getStockProfile(symbol, region,
            object : Callback<JsonObject?> {
                override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                    if (response.code() == 200 || response.code() == 204) {
                        try {
                            val jsonObject = JSONObject(response.body().toString())
                            val priceJSONArray = jsonObject.getJSONObject("price").toString()
                            val price: Price = Gson().fromJson(priceJSONArray, Price::class.java)

                            //10초마다 한번씩 메시지 보내기.
                            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                                val bundle = Bundle()
                                bundle.putString("currentPrice", price.regularMarketPrice.raw.toString())
                                val msg = Message.obtain(null, MSG_SEND_TO_ACTIVITY)
                                msg.data = bundle
                                mClient.send(msg)
                            },1000)

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        } catch (e: RemoteException){
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                    Log.d("RequestResult", "RetrofitExample, Type : get, Result : onFailure, Error Message : " + t.message)
                    val msg = Message.obtain(null, MSG_SEND_TO_ACTIVITY)
                    mClient.send(msg)
                }
            })
    }
}