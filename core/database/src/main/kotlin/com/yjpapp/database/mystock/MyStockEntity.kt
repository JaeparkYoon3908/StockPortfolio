package com.yjpapp.database.mystock

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "my_stock")
data class MyStockEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0 //id
    , @ColumnInfo(name = "subjectName") val subjectName: String = "" //종목명
    , @ColumnInfo(name = "subjectCode") val subjectCode: String = "" //종목코드
    , @ColumnInfo(name = "purchaseDate") val purchaseDate: String = "" //매수일
    , @ColumnInfo(name = "purchasePrice") val purchasePrice: String = "" //평균단가
    , @ColumnInfo(name = "purchaseCount") val purchaseCount: Int = 0 //보유수량
    , @ColumnInfo(name = "type") val type: Int = 0 // 1 = 한국주식, 2 = 미국주식
    //실시간 변동 데이터
    , @ColumnInfo(name = "currentPrice") val currentPrice: String = "0" //종가
    , @ColumnInfo(name = "dayToDayPrice") val dayToDayPrice: String = "0" //전일 대비 변동 가격
    , @ColumnInfo(name = "dayToDayPercent") val dayToDayPercent: String = "" //현재 상승 퍼센트
    , @ColumnInfo(name = "basDt") val basDt: String = "" //기준일자
)