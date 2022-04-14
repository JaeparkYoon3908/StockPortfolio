package com.yjpapp.stockportfolio.base

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.appcompat.app.AppCompatActivity
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.common.dialog.CommonDialogManager

abstract class BaseActivity : AppCompatActivity() {
    private val TAG = BaseActivity::class.java.simpleName
    /**
     * 네트워크 연결에러 체크
     */
    private val connectivityManager by lazy {
        applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
    private var isShowDialog = false
    private val networkConnectedCallBack by lazy {
        object : ConnectivityManager.NetworkCallback() {
            override fun onLost(network: Network) {
                super.onLost(network)
                CommonDialogManager.showCommonOneBtnDialog(
                this@BaseActivity,
                    supportFragmentManager,
                    TAG,
                    getString(R.string.Error_Msg_Network_Connect_Exception)
                )
            }

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()
        isShowDialog = false
        val networkRequest = NetworkRequest.Builder() // addTransportType : 주어진 전송 요구 사항을 빌더에 추가
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR) // TRANSPORT_CELLULAR : 이 네트워크가 셀룰러 전송을 사용함을 나타냅니다.
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI) // TRANSPORT_WIFI : 이 네트워크가 Wi-Fi 전송을 사용함을 나타냅니다.
            .build()
        connectivityManager.registerNetworkCallback(networkRequest, networkConnectedCallBack)
    }

    override fun onDestroy() {
        super.onDestroy()
        connectivityManager.unregisterNetworkCallback(networkConnectedCallBack)
    }
}