package com.yjpapp.stockportfolio.model

sealed class TabData(
    val title: String,
    val route: String,
    val url: String
) {
    /**
     * MyStock Tab
     */
    data object KoreaStock: TabData(title = "한국주식", route = "KoreaStock", url = "")
    data object USAStock: TabData(title = "미국주식", route = "KoreaStock", url = "")

    /**
     * News Tab
     */
    data object MKNews : TabData(title = "매일경제", route = "MKNews", url = "https://www.mk.co.kr/rss/30100041/")
    data object HanKyungNews : TabData(title = "한국경제", route = "HanKyungNews", url = "https://www.hankyung.com/feed/finance")
    data object FinancialNews : TabData(title = "파이낸셜뉴스", route = "FinancialNews", url = "https://www.fnnews.com/rss/r20/fn_realnews_stock.xml")
}