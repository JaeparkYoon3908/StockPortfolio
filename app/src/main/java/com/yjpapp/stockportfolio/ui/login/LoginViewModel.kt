package com.yjpapp.stockportfolio.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel

/**
 * @author 윤재박
 * @since 2021.07
 */
class LoginViewModel(application: Application): AndroidViewModel(application) {
    enum class LoginType{
        GOOGLE,
        NAVER,
        KAKAO
    }

    var loginType = LoginType.GOOGLE
}