package com.yjpapp.database.mystock

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "my_stock")
data class MyStockEntity (
    @PrimaryKey(autoGenerate = true) var id: Int = 0 //id
    , @ColumnInfo(name = "subjectName") var subjectName: String = "" //종목명
    , @ColumnInfo(name = "subjectCode") var subjectCode: String = "" //종목코드
//    , @ColumnInfo(name = "purchaseDate") var purchaseDate: String = "" //매수일
    , @ColumnInfo(name = "purchasePrice") var purchasePrice: String = "" //평균단가
    , @ColumnInfo(name = "purchaseCount") var purchaseCount: Int = 0 //보유수량
    //실시간 변동 데이터
    , @ColumnInfo(name = "currentPrice") var currentPrice: String = "0" //종가
    , @ColumnInfo(name = "dayToDayPrice") var dayToDayPrice: String = "0" //전일 대비 변동 가격
    , @ColumnInfo(name = "dayToDayPercent") var dayToDayPercent: String = "" //현재 상승 퍼센트
    , @ColumnInfo(name = "basDt") var basDt: String = "" //기준일자
)