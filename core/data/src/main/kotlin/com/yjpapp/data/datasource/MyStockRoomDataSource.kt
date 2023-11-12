package com.yjpapp.data.datasource

import com.yjpapp.database.mystock.MyStockEntity
import com.yjpapp.database.mystock.MyStockDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MyStockRoomDataSource @Inject constructor(
    private val myStockDao: MyStockDao,
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

    suspend fun requestUpdateMyStock(myStockEntity: MyStockEntity): Boolean {
        return withContext(ioDispatcher) {
            try {
                myStockDao.update(myStockEntity)
                true
            } catch (e: Exception) {
                e.stackTrace
                false
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

    suspend fun requestGetAllMyStock(): List<MyStockEntity> =
        withContext(Dispatchers.IO) {
            try {
                myStockDao.getAll()
            } catch (e: Exception) {
                e.stackTrace
                listOf()
            }
        }
}