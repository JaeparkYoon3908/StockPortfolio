package com.yjpapp.stockportfolio.function.my

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.yjpapp.data.repository.MyRepository
import com.yjpapp.data.datasource.UserDataSource
import com.yjpapp.data.repository.UserRepository
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.base.BaseViewModel
import com.yjpapp.stockportfolio.common.StockConfig
import com.yjpapp.stockportfolio.extension.EventFlow
import com.yjpapp.stockportfolio.extension.MutableEventFlow
import com.yjpapp.stockportfolio.util.PatternUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userRepository: UserRepository,
    private val myRepository: MyRepository
): BaseViewModel() {
    private val _uiState = MutableEventFlow<Event>()
    val uiState: EventFlow<Event> get() = _uiState
    val userName = userRepository.getUserName()
    val userEmail = PatternUtils.getEmailMasking(userRepository.getUserEmail())
    val userLoginType = userRepository.getLoginType()
    val isMyStockAutoRefresh = myRepository.getMyStockAutoRefresh()
    val isMyStockAutoAdd = myRepository.getMyStockAutoAdd()
    val isMyStockShowDeleteCheck = myRepository.getMyStockShowDeleteCheck()
    val isIncomeNoteShowDeleteCheck = myRepository.getIncomeNoteShowDeleteCheck()
    val isAutoLoginCheck = myRepository.getAutoLogin()
    val isShowMemoDeleteCheck = myRepository.getShowMemoDeleteCheck()
    val isMemoLongClickVibrateCheck = myRepository.getMemoVibrateOff()

    fun requestLogout() {
        event(Event.StartLoadingAnimation(""))
        when (userRepository.getLoginType()) {
            StockConfig.LOGIN_TYPE_NAVER -> {
                NidOAuthLogin().callDeleteTokenApi(context, object : OAuthLoginCallback {
                    override fun onSuccess() {
                        //서버에서 토큰 삭제에 성공한 상태입니다.
                        userRepository.logout()
                        event(Event.StopLoadingAnimation(""))
                        event(Event.StartLoginActivity(""))
                    }
                    override fun onFailure(httpStatus: Int, message: String) {
                        // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
                        // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.
                        event(Event.StopLoadingAnimation(""))
                        event(Event.ResponseServerError(message))

                    }
                    override fun onError(errorCode: Int, message: String) {
                        // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
                        // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.
                        onFailure(errorCode, message)
                    }
                })
            }
            StockConfig.LOGIN_TYPE_GOOGLE, StockConfig.LOGIN_TYPE_FACEBOOK -> {
                userRepository.logout()
                event(Event.StartLoginActivity(""))
            }
        }
    }
    fun requestMemberOff() {
        viewModelScope.launch {
            event(Event.StartLoadingAnimation(""))
            val result = userRepository.deleteUserInfo()
            if (result == null) {
                event(Event.ResponseServerError(context.getString(R.string.Error_Msg_Network_Connect_Exception)))
                return@launch
            }

            when (userRepository.getLoginType()) {
                StockConfig.LOGIN_TYPE_NAVER -> {
                    NidOAuthLogin().callDeleteTokenApi(context, object : OAuthLoginCallback {
                        override fun onSuccess() {
                            event(Event.StopLoadingAnimation(""))
                            if (!result.isSuccessful) {
                                event(Event.ResponseServerError("서버에 응답이 없습니다. 잠시 후 다시 시도해주세요."))
                                return
                            }
                            requestDeleteUserInfo()
                            event(Event.StartLoginActivity(""))
                        }
                        override fun onFailure(httpStatus: Int, message: String) {
                            event(Event.StopLoadingAnimation(""))
                            event(Event.ResponseServerError(message))
                        }
                        override fun onError(errorCode: Int, message: String) {
                            onFailure(errorCode, message)
                        }
                    })
                }
                StockConfig.LOGIN_TYPE_GOOGLE, StockConfig.LOGIN_TYPE_FACEBOOK -> {
                    if (!result.isSuccessful) {
                        event(Event.ResponseServerError("서버에 응답이 없습니다. 잠시 후 다시 시도해주세요."))
                        return@launch
                    }
                    requestDeleteUserInfo()
                    event(Event.StopLoadingAnimation(""))
                    event(Event.StartLoginActivity(""))
                    return@launch

                }
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

    private fun event(event: Event) {
        viewModelScope.launch {
            _uiState.emit(event)
        }
    }

    sealed class Event {
//        data class ShowInfoToastMessage(val msg: String): Event()
//        data class ShowErrorToastMessage(val msg: String): Event()
        data class StartLoadingAnimation(val msg: String): Event()
        data class StopLoadingAnimation(val msg: String): Event()
        data class StartLoginActivity(val msg: String): Event()
        data class ResponseServerError(val msg: String): Event()
    }
}