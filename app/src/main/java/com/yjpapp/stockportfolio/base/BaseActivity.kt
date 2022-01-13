package com.yjpapp.stockportfolio.base

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.yjpapp.stockportfolio.localdb.preference.PreferenceController
import com.yjpapp.stockportfolio.network.ResponseAlertManger
import org.koin.android.ext.android.inject

abstract class BaseActivity<T : ViewDataBinding>(
    private val layoutId: Int
) : AppCompatActivity() {
    private var _binding: T? = null
    val binding: T get() = _binding!!
    val preferenceController: PreferenceController by inject()

    /**
     * 네트워크 연결에러 체크
     */
    private val connectivityManager by lazy {
        applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
    private val networkConnectedCallBack by lazy {
        object : ConnectivityManager.NetworkCallback() {
            override fun onLost(network: Network) {
                super.onLost(network)
                ResponseAlertManger.showNetworkConnectErrorAlert(this@BaseActivity)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, layoutId)
    }

    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()
        val networkRequest = NetworkRequest.Builder() // addTransportType : 주어진 전송 요구 사항을 빌더에 추가
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR) // TRANSPORT_CELLULAR : 이 네트워크가 셀룰러 전송을 사용함을 나타냅니다.
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI) // TRANSPORT_WIFI : 이 네트워크가 Wi-Fi 전송을 사용함을 나타냅니다.
            .build()
        connectivityManager.registerNetworkCallback(networkRequest, networkConnectedCallBack)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
        connectivityManager.unregisterNetworkCallback(networkConnectedCallBack)
    }
}