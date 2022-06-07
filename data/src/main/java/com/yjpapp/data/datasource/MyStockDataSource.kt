package com.yjpapp.data.datasource

import com.yjpapp.data.localdb.room.mystock.MyStockDao
import com.yjpapp.data.localdb.room.mystock.MyStockEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MyStockDataSource(private val myStockDao: MyStockDao) {
    private val ioDispatcher = Dispatchers.IO
    suspend fun requestInsertMyStock(myStockEntity: MyStockEntity) {
        withContext(ioDispatcher) {
            myStockDao.insert(myStockEntity)
        }
    }

    suspend fun requestUpdateMyStock(myStockEntity: MyStockEntity) {
        withContext(ioDispatcher) {
            myStockDao.update(myStockEntity)
        }
    }

    suspend fun requestDeleteMyStock(myStockEntity: MyStockEntity) {
        withContext(ioDispatcher) {
            myStockDao.delete(myStockEntity)
        }
    }

    suspend fun requestGetAllMyStock(): MutableList<MyStockEntity> =
        withContext(Dispatchers.IO) {
            myStockDao.getAll()
        }
}