package com.yjpapp.stockportfolio.ui.mystock

import com.yjpapp.stockportfolio.base.BaseRepository
import com.yjpapp.stockportfolio.database.room.MyStockDao
import com.yjpapp.stockportfolio.database.room.MyStockEntity

class MyStockRepository(private val myStockDao: MyStockDao): BaseRepository() {
    fun insertMyStock(myStockEntity: MyStockEntity){
        myStockDao.insert(myStockEntity)
    }

    fun updateMyStock(myStockEntity: MyStockEntity){
        myStockDao.update(myStockEntity)
    }

    fun deleteMyStock(myStockEntity: MyStockEntity){
        myStockDao.delete(myStockEntity)
    }


}