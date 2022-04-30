package com.yjpapp.stockportfolio.common

import java.util.*

object StockConfig {
    /**
     * Common
     */
    //확장성을 고려하면 나라 코드별로 symbol을 만드는 코드를 집어넣어야 하지만 지금은 한국위주로..
    val koreaMoneySymbol: String = Currency.getInstance(Locale.KOREA).symbol
    val usaMoneySymbol: String = Currency.getInstance(Locale.US).symbol
    const val LOCAL_KOREA = "kr"
    const val LOCAL_USA = "usa"

    /**
     * 구글 아이디로 로그인
     */
    const val LOGIN_TYPE_GOOGLE = "GOOGLE"

    /**
     * 네이버 아이디로 로그인
     */
    const val LOGIN_TYPE_NAVER = "NAVER"
    const val NAVER_SIGN_CLIENT_ID = "j9SRSjKtQ1bueHqgd1pv"
    const val NAVER_SIGN_CLIENT_SECRET = "5QuEg4cvmA"

    /**
     * 페이스북 아이디로 로그인
     */
    const val LOGIN_TYPE_FACEBOOK = "FACEBOOK"

    const val TRUE = "true"
    const val FALSE = "false"

    /**
     * AdMob
     */
    const val TEST_AD_MOB_ID = "ca-app-pub-3940256099942544/6300978111"
}