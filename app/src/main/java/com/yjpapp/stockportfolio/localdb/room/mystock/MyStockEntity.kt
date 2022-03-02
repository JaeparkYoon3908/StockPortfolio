package com.yjpapp.stockportfolio.localdb.room.mystock

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
    , @ColumnInfo(name = "purchaseCount") var purchaseCount: String = "" //보유수량
    , @ColumnInfo(name = "currentPrice") var currentPrice: String = "" //현재가
    )
