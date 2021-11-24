package com.yjpapp.stockportfolio.function.my

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.constance.StockConfig
import com.yjpapp.stockportfolio.localdb.preference.PrefKey
import com.yjpapp.stockportfolio.localdb.preference.PreferenceController
import com.yjpapp.stockportfolio.model.response.RespNaverDeleteUserInfo
import com.yjpapp.stockportfolio.network.ResponseAlertManger
import com.yjpapp.stockportfolio.repository.UserRepository
import com.yjpapp.stockportfolio.util.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyViewModel(
    application: Application,
    private val preferenceController: PreferenceController,
    private val userRepository: UserRepository
): AndroidViewModel(application) {
    val userName = preferenceController.getPreference(PrefKey.KEY_USER_NAME)
    val userEmail = Utils.getEmailMasking(preferenceController.getPreference(PrefKey.KEY_USER_EMAIL))
    val userLoginType = preferenceController.getPreference(PrefKey.KEY_USER_LOGIN_TYPE)
    val isMyStockAutoRefresh = preferenceController.getPreference(PrefKey.KEY_SETTING_MY_STOCK_AUTO_REFRESH)
    val isMyStockAutoAdd = preferenceController.getPreference(PrefKey.KEY_SETTING_MY_STOCK_AUTO_ADD)
    val isMyStockShowDeleteCheck = preferenceController.getPreference(PrefKey.KEY_SETTING_MY_STOCK_SHOW_DELETE_CHECK)
    val isIncomeNoteShowDeleteCheck = preferenceController.getPreference(PrefKey.KEY_SETTING_INCOME_NOTE_SHOW_DELETE_CHECK)
    val isAutoLoginCheck = preferenceController.getPreference(PrefKey.KEY_SETTING_AUTO_LOGIN)
    val isAutoMemoShowDeleteCheck = preferenceController.getPreference(PrefKey.KEY_SETTING_MEMO_SHOW_DELETE_CHECK)
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
    fun requestDeleteNaverUserInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            val naverAccessToken = preferenceController.getPreference(PrefKey.KEY_NAVER_ACCESS_TOKEN)?: ""
            val params = HashMap<String, String>()
            params["client_id"] = StockConfig.NAVER_SIGN_CLIENT_ID
            params["client_secret"] = StockConfig.NAVER_SIGN_CLIENT_SECRET
            params["access_token"] = naverAccessToken
            params["grant_type"] = "delete"
            params["service_provider"] = "NAVER"

            val result = userRepository.deleteNaverUserInfo(getApplication(), params)
            if (result == null) {
                isNetworkConnectException.value = true
                return@launch
            }
            if (result.isSuccessful) {
                respDeleteNaverUserInfo.value = result.body()
            }
        }
    }
}