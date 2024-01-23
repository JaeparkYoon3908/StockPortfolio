package com.yjpapp.data.mapper

import com.yjpapp.data.model.MyStockData
import com.yjpapp.database.mystock.MyStockEntity
fun MyStockData.mapping(): MyStockEntity =
    MyStockEntity(
        id = this.id,
        subjectName = this.subjectName,
        subjectCode = this.subjectCode,
//        purchaseDate = this.purchaseDate,
        purchasePrice = this.purchasePrice,
        purchaseCount = this.purchaseCount,
        currentPrice = this.currentPrice,
        dayToDayPrice = this.dayToDayPrice,
        dayToDayPercent = this.dayToDayPercent,
        basDt = this.basDt
    )

fun MyStockEntity.mapping(): MyStockData =
    MyStockData(
        id = this.id,
        subjectName = this.subjectName,
        subjectCode = this.subjectCode,
//        purchaseDate = this.purchaseDate,
        purchasePrice = this.purchasePrice,
        purchaseCount = this.purchaseCount,
        currentPrice = this.currentPrice,
        dayToDayPrice = this.dayToDayPrice,
        dayToDayPercent = this.dayToDayPercent,
        basDt = this.basDt
    )