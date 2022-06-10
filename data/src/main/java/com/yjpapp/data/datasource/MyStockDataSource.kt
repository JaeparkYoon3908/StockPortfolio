package com.yjpapp.data.datasource

import com.yjpapp.data.localdb.room.mystock.MyStockDao
import com.yjpapp.data.localdb.room.mystock.MyStockEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MyStockDataSource(
    private val myStockDao: MyStockDao
) {
    private val ioDispatcher = Dispatchers.IO
    suspend fun requestInsertMyStock(myStockEntity: MyStockEntity) {
        withContext(ioDispatcher) {
            try {
                myStockDao.insert(myStockEntity)
            } catch (e: Exception) {
                e.stackTrace
            }
        }
    }

    suspend fun requestUpdateMyStock(myStockEntity: MyStockEntity) {
        withContext(ioDispatcher) {
            try {
                myStockDao.update(myStockEntity)
            } catch (e: Exception) {
                e.stackTrace
            }
        }
    }

    suspend fun requestDeleteMyStock(myStockEntity: MyStockEntity) {
        withContext(ioDispatcher) {
            try {
                myStockDao.delete(myStockEntity)
            } catch (e: Exception) {
                e.stackTrace
            }
        }
    }

    suspend fun requestGetAllMyStock(): MutableList<MyStockEntity> =
        withContext(Dispatchers.IO) {
            try {
                myStockDao.getAll()
            } catch (e: Exception) {
                e.stackTrace
                mutableListOf()
            }
        }
}