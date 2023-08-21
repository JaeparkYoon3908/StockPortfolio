package com.yjpapp.stockportfolio.data.repository

import com.yjpapp.stockportfolio.data.datasource.MyStockDataSource
import com.yjpapp.stockportfolio.data.localdb.room.mystock.MyStockEntity
import javax.inject.Inject

interface MyStockRepository {
    suspend fun addMyStock(myStockEntity: MyStockEntity)
    suspend fun updateMyStock(myStockEntity: MyStockEntity)
    suspend fun deleteMyStock(myStockEntity: MyStockEntity)
    suspend fun getAllMyStock(): MutableList<MyStockEntity>
}
class MyStockRepositoryImpl @Inject constructor(
    private val myStockDataSource: MyStockDataSource,
): MyStockRepository {

    override suspend fun addMyStock(myStockEntity: MyStockEntity) {
        myStockDataSource.requestInsertMyStock(myStockEntity)
    }

    override suspend fun updateMyStock(myStockEntity: MyStockEntity) {
        myStockDataSource.requestUpdateMyStock(myStockEntity)
    }

    override suspend fun deleteMyStock(myStockEntity: MyStockEntity) {
        myStockDataSource.requestDeleteMyStock(myStockEntity)
    }

    override suspend fun getAllMyStock() = myStockDataSource.requestGetAllMyStock()
}