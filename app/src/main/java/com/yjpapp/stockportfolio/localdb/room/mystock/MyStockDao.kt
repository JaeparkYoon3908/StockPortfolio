package com.yjpapp.stockportfolio.localdb.room.mystock

import androidx.room.*

@Dao
interface MyStockDao {

    @Insert
    fun insert(myStockEntity: MyStockEntity)

    @Update
    fun update(myStockEntity: MyStockEntity)

    @Delete
    fun delete(myStockEntity: MyStockEntity)

    //Query
    @Query("SELECT * FROM my_stock")
    fun getAll(): MutableList<MyStockEntity>

    @Query("SELECT * FROM my_stock WHERE subjectName = :subjectName")
    fun getSubjectName(subjectName: String): MyStockEntity
}