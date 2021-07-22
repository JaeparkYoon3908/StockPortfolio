package com.yjpapp.stockportfolio.function.mystock

import com.yjpapp.stockportfolio.localdb.room.MyStockDao
import com.yjpapp.stockportfolio.localdb.room.MyStockEntity

class MyStockRepository(private val myStockDao: MyStockDao) {
    fun insertMyStock(myStockEntity: MyStockEntity) {
        myStockDao.insert(myStockEntity)
    }

    fun updateMyStock(myStockEntity: MyStockEntity) {
        myStockDao.update(myStockEntity)
    }

    fun deleteMyStock(myStockEntity: MyStockEntity) {
        myStockDao.delete(myStockEntity)
    }

    fun getAllMyStock(): MutableList<MyStockEntity> {
        return myStockDao.getAll()
    }
}