package com.yjpapp.stockportfolio.constance

import java.util.*

object AppConfig {
    val x_rapidapi_key = "bb73bc78demsh6672d9db8475236p16845djsn96ceabb579fc"
    val x_rapidapi_host: String = "apidojo-yahoo-finance-v1.p.rapidapi.com"

    //확장성을 고려하면 나라 코드별로 symbol을 만드는 코드를 집어넣어야 하지만 지금은 한국위주로..
    val moneySymbol: String = Currency.getInstance(Locale.KOREA).symbol
}