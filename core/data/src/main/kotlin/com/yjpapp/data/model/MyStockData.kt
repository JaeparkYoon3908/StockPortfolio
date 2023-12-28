package com.yjpapp.data.model

import com.yjpapp.database.mystock.MyStockEntity

data class MyStockData(
    val id: Int = 0, //id
    val subjectName: String = "", //종목명
    val subjectCode: String = "", //종목코드
    val purchaseDate: String = "", //매수일
    val purchasePrice: String = "", //평균단가
    val purchaseCount: Int = 0, //보유수량
    val type: Int = 0, //1: 한국주식, 2: 미국주식
    //실시간 변동 데이터
    val currentPrice: String = "0", //종가
    val dayToDayPrice: String = "0", //전일 대비 변동 가격
    val dayToDayPercent: String = "", //현재 상승 퍼센트
    val basDt: String = "" //기준일자
)