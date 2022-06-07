package com.yjpapp.data.repository

import com.yjpapp.data.datasource.MyStockDataSource
import com.yjpapp.data.localdb.room.mystock.MyStockEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MyStockRepository @Inject constructor(
    private val myStockDataSource: MyStockDataSource,
) : BaseRepository() {

    suspend fun addMyStock(myStockEntity: MyStockEntity) {
        myStockDataSource.requestInsertMyStock(myStockEntity)
    }

    suspend fun updateMyStock(myStockEntity: MyStockEntity) {
        myStockDataSource.requestUpdateMyStock(myStockEntity)
    }

    suspend fun deleteMyStock(myStockEntity: MyStockEntity) {
        myStockDataSource.requestDeleteMyStock(myStockEntity)
    }

    suspend fun getAllMyStock(): MutableList<MyStockEntity> =
        myStockDataSource.requestGetAllMyStock()
}