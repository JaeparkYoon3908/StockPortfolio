package com.yjpapp.stockportfolio.database.room

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
    @Query("SELECT * FROM MY_STOCK")
    fun getAll(): MutableList<MyStockEntity>

    @Query("SELECT * FROM MY_STOCK WHERE subjectName = :subjectName")
    fun getSubjectName(subjectName: String): MyStockEntity
}