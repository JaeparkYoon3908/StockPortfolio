package com.yjpapp.stockportfolio.common

import java.util.*

object StockConfig {
    /**
     * Common
     */
    //확장성을 고려하면 나라 코드별로 symbol을 만드는 코드를 집어넣어야 하지만 지금은 한국위주로..
    val moneySymbol: String = Currency.getInstance(Locale.KOREA).symbol

    /**
     * 구글 아이디로 로그인
     */
    val LOGIN_TYPE_GOOGLE = "GOOGLE"
    val GOOGLE_SIGN_CLIENT_ID = "248238742829-c4l2mo77psv2f681sm0lta392hur63fk.apps.googleusercontent.com"

    /**
     * 네이버 아이디로 로그인
     */
    val LOGIN_TYPE_NAVER = "NAVER"
    val NAVER_SIGN_CLIENT_ID = "j9SRSjKtQ1bueHqgd1pv"
    val NAVER_SIGN_CLIENT_SECRET = "5QuEg4cvmA"

    /**
     * 페이스북 아이디로 로그인
     */
    val LOGIN_TYPE_FACEBOOK = "FACEBOOK"
    val FACEBOOK_SIGN_APP_ID = "2928940814012749"
    val FACEBOOK_SIGN_PROTOCOL_SCHEME = "fb2928940814012749"

    val TRUE = "true"
    val FALSE = "false"

    /**
     * AdMob
     */
    val AD_MOB_ID = "pub-6147567013464925"
    val TEST_AD_MOB_ID = "ca-app-pub-3940256099942544/6300978111"
}