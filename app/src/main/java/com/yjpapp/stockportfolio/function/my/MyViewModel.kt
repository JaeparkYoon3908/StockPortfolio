package com.yjpapp.stockportfolio.function.my

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yjpapp.stockportfolio.constance.StockConfig
import com.yjpapp.stockportfolio.localdb.preference.PrefKey
import com.yjpapp.stockportfolio.model.response.RespNaverDeleteUserInfo
import com.yjpapp.stockportfolio.network.ResponseAlertManger
import com.yjpapp.stockportfolio.repository.MyRepository
import com.yjpapp.stockportfolio.repository.UserRepository
import com.yjpapp.stockportfolio.util.Utils
import kotlinx.coroutines.launch

class MyViewModel(
    private val userRepository: UserRepository,
    private val myRepository: MyRepository
): ViewModel() {
    val userName = userRepository.getUserName()
    val userEmail = Utils.getEmailMasking(userRepository.getUserEmail())
    val userLoginType = userRepository.getLoginType()
    val isMyStockAutoRefresh = myRepository.getMyStockAutoRefresh()
    val isMyStockAutoAdd = myRepository.getMyStockAutoAdd()
    val isMyStockShowDeleteCheck = myRepository.getMyStockShowDeleteCheck()
    val isIncomeNoteShowDeleteCheck = myRepository.getIncomeNoteShowDeleteCheck()
    val isAutoLoginCheck = myRepository.getAutoLogin()
    val isShowMemoDeleteCheck = myRepository.getShowMemoDeleteCheck()
    val isMemoLongClickVibrateCheck = myRepository.getMemoVibrateOff()

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
            val naverAccessToken = userRepository.getNaverAccessToken()
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
        myRepository.setMyStockAutoRefresh(isAutoRefresh)
    }
    fun requestMyStockAutoAdd(isAutoAdd: Boolean) {
        myRepository.setMyStockAutoAdd(isAutoAdd)
    }
    fun requestMyStockShowDeleteCheck(isDeleteCheckShow: Boolean) {
        myRepository.setMyStockShowDeleteCheck(isDeleteCheckShow)
    }
    fun requestIncomeNoteShowDeleteCheck(isDeleteCheckShow: Boolean) {
        myRepository.setIncomeNoteShowDeleteCheck(isDeleteCheckShow)
    }
    fun requestMemoShowDeleteCheck(isDeleteCheckShow: Boolean) {
        myRepository.setShowMemoDeleteCheck(isDeleteCheckShow)
    }
    fun requestMemoVibrateOff(isVibrateOff: Boolean) {
        myRepository.setMemoVibrateOff(isVibrateOff)
    }
    fun requestDeleteUserInfo() {
        myRepository.deleteUserPreference()
    }
    fun getLoginType(): String {
        return userRepository.getLoginType()
    }
}