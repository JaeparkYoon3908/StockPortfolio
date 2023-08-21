package com.yjpapp.stockportfolio.data.localdb.room.mystock

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "my_stock")
data class MyStockEntity (
    @PrimaryKey(autoGenerate = true) var id: Int = 0 //id
    , @ColumnInfo(name = "subjectName") var subjectName: String = "" //종목명
    , @ColumnInfo(name = "subjectCode") var subjectCode: String = "" //종목코드
    , @ColumnInfo(name = "purchaseDate") var purchaseDate: String = "" //매수일
    , @ColumnInfo(name = "purchasePrice") var purchasePrice: String = "" //평균단가
    , @ColumnInfo(name = "purchaseCount") var purchaseCount: Int = 0 //보유수량
    //실시간 변동 데이터
    , @ColumnInfo(name = "currentPrice") var currentPrice: String = "" //현재가
    , @ColumnInfo(name = "gainPrice") var gainPrice: String = "" //수익
    , @ColumnInfo(name = "dayToDayPrice") var dayToDayPrice: String = "" //전일 대비 변동 가격
    , @ColumnInfo(name = "dayToDayPercent") var dayToDayPercent: String = "" //현재 상승 퍼센트
    , @ColumnInfo(name = "yesterdayPrice") var yesterdayPrice: String = "" //어제 가격

    ) {
    data class CurrentPriceData(
        var currentPrice: String = "",
        var dayToDayPrice: String = "",
        var dayToDayPercent: String = "",
        var yesterdayPrice: String = "",
    )
}
