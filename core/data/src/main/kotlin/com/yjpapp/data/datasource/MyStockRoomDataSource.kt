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
            myStockDao.insert(myStockEntity)
        }
    }

    suspend fun requestUpdateMyStock(myStockEntity: MyStockEntity) {
        return withContext(ioDispatcher) {
            myStockDao.update(myStockEntity)
        }
    }

    suspend fun requestDeleteMyStock(myStockEntity: MyStockEntity) {
        withContext(ioDispatcher) {
            myStockDao.delete(myStockEntity)
        }
    }

    suspend fun requestGetAllMyStock(): List<MyStockEntity> =
        withContext(Dispatchers.IO) {
            myStockDao.getAll()
        }
}