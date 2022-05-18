package com.yjpapp.data.repository

import com.yjpapp.data.localdb.room.mystock.MyStockDao
import com.yjpapp.data.localdb.room.mystock.MyStockEntity

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