package com.yjpapp.stockportfolio.base

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.common.dialog.CommonDialogManager
import com.yjpapp.stockportfolio.common.dialog.CommonOneBtnDialog

abstract class BaseActivity : AppCompatActivity() {
    private val TAG = BaseActivity::class.java.simpleName
    /**
     * 네트워크 연결에러 체크
     */
    private val connectivityManager by lazy {
        applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
    private val networkDisConnectDialogFragment by lazy {
        CommonOneBtnDialog(
            this@BaseActivity,
            CommonOneBtnDialog.CommonOneBtnData(
                noticeText = getString(R.string.Error_Msg_Network_Connect_Exception),
                btnText = getString(R.string.Common_Ok),
                btnListener = { _: View, dialog: CommonOneBtnDialog ->
                    dialog.dismiss()
                }
            )
        )
    }
    private var isShowDialog = false
    private val networkConnectedCallBack by lazy {
        object : ConnectivityManager.NetworkCallback() {
            override fun onLost(network: Network) {
                super.onLost(network)
                if (networkDisConnectDialogFragment.dialog == null) {
                    return
                }
                if (networkDisConnectDialogFragment.dialog?.isShowing == true) {
                    return
                }
                if (!networkDisConnectDialogFragment.isRemoving) {
                    return
                }
                networkDisConnectDialogFragment.show(supportFragmentManager, TAG)

            }

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                if (networkDisConnectDialogFragment.dialog != null) {
                    return
                }
                if (networkDisConnectDialogFragment.dialog?.isShowing == false) {
                    return
                }
                if (networkDisConnectDialogFragment.isRemoving) {
                    return
                }
                networkDisConnectDialogFragment.dismiss()
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