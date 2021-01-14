package com.yjpapp.stockportfolio.ui.service

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log


class MyStockService: Service() {
    companion object {
        const val MSG_SEND_TO_ACTIVITY = 0
        const val MSG_SEND_TO_SERVICE = 1
        const val MSG_REGISTER_CLIENT = 2
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

//        val symbol = "005930.KS"
//        val region = "KR"
//        YahooFinanceProtocolManager.getInstance(applicationContext).getStockProfile(symbol, region,
//                object : Callback<JsonObject?> {
//                    override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
//                        if (response.code() == 200 || response.code() == 204) {
//                            try {
//                                val jsonObject = JSONObject(response.body().toString())
//                                val priceJSONArray = jsonObject.getJSONObject("price").toString()
//                                val price: Price = Gson().fromJson(priceJSONArray, Price::class.java)
//                                Log.d("YJP", "price = $price")
//                            } catch (e: JSONException) {
//                                e.printStackTrace()
//                            }
//                        }
//                    }
//
//                    override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
//                        Log.d("RequestResult", "RetrofitExample, Type : get, Result : onFailure, Error Message : " + t.message)
//                    }
//                })

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

}