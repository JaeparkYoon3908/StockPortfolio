package com.yjpapp.stockportfolio.function.my

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yjpapp.stockportfolio.constance.StockConfig
import com.yjpapp.stockportfolio.localdb.preference.PrefKey
import com.yjpapp.stockportfolio.localdb.preference.PreferenceController
import com.yjpapp.stockportfolio.model.response.RespNaverDeleteUserInfo
import com.yjpapp.stockportfolio.network.ResponseAlertManger
import com.yjpapp.stockportfolio.repository.MyRepository
import com.yjpapp.stockportfolio.repository.UserRepository
import com.yjpapp.stockportfolio.util.Utils
import kotlinx.coroutines.launch

class MyViewModel(
    private val preferenceController: PreferenceController,
    private val userRepository: UserRepository,
    private val myRepository: MyRepository
): ViewModel() {
    val userName = preferenceController.getPreference(PrefKey.KEY_USER_NAME)
    val userEmail = Utils.getEmailMasking(preferenceController.getPreference(PrefKey.KEY_USER_EMAIL))
    val userLoginType = preferenceController.getPreference(PrefKey.KEY_USER_LOGIN_TYPE)
    val isMyStockAutoRefresh = preferenceController.getPreference(PrefKey.KEY_SETTING_MY_STOCK_AUTO_REFRESH)
    val isMyStockAutoAdd = preferenceController.getPreference(PrefKey.KEY_SETTING_MY_STOCK_AUTO_ADD)
    val isMyStockShowDeleteCheck = preferenceController.getPreference(PrefKey.KEY_SETTING_MY_STOCK_SHOW_DELETE_CHECK)
    val isIncomeNoteShowDeleteCheck = preferenceController.getPreference(PrefKey.KEY_SETTING_INCOME_NOTE_SHOW_DELETE_CHECK)
    val isAutoLoginCheck = preferenceController.getPreference(PrefKey.KEY_SETTING_AUTO_LOGIN)
    val isAutoMemoShowDeleteCheck = preferenceController.getPreference(PrefKey.KEY_SETTING_MEMO_SHOW_DELETE_CHECK)
    val isMemoLongClickVibrateCheck = preferenceController.getPreference(PrefKey.KEY_SETTING_MEMO_LONG_CLICK_VIBRATE_CHECK)

    val isMemberOffSuccess = MutableLiveData<Boolean>()
    val isNetworkConnectException = MutableLiveData<Boolean>()

    fun requestLogout(context: Context) {
        userRepository.logout(context)
    }
    fun requestMemberOff(context: Context) {
        viewModelScope.launch {
            val result = userRepository.deleteUserInfo(context)
            if (result == null) {
                ResponseAlertManger.showNetworkConnectErrorAlert(context)
                return@launch
            }
            if (result.isSuccessful) {
                isMemberOffSuccess.value = true
            }
        }
    }

    val respDeleteNaverUserInfo = MutableLiveData<RespNaverDeleteUserInfo>()
    fun requestDeleteNaverUserInfo(context: Context) {
        viewModelScope.launch {
            val naverAccessToken = preferenceController.getPreference(PrefKey.KEY_NAVER_ACCESS_TOKEN)?: ""
            val params = HashMap<String, String>()
            params["client_id"] = StockConfig.NAVER_SIGN_CLIENT_ID
            params["client_secret"] = StockConfig.NAVER_SIGN_CLIENT_SECRET
            params["access_token"] = naverAccessToken
            params["grant_type"] = "delete"
            params["service_provider"] = "NAVER"

            val result = userRepository.deleteNaverUserInfo(context, params)
            if (result == null) {
                ResponseAlertManger.showNetworkConnectErrorAlert(context)
                return@launch
            }
            if (result.isSuccessful) {
                respDeleteNaverUserInfo.postValue(result.body())
            }
        }
    }

    fun requestSetAutoLogin(isAutoLogin: Boolean) {
        myRepository.setAutoLogin(isAutoLogin)
    }
    fun requestMyStockSetAutoRefresh(isAutoRefresh: Boolean) {
        myRepository.setMyStockSetAutoRefresh(isAutoRefresh)
    }
}