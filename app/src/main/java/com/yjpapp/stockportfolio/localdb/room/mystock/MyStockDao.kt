package com.yjpapp.stockportfolio.localdb.room.mystock

import androidx.room.*
import kotlin.jvm.Throws

@Dao
interface MyStockDao {

    @Insert
    @Throws(Exception::class)
    fun insert(myStockEntity: MyStockEntity)

    @Update
    @Throws(Exception::class)
    fun update(myStockEntity: MyStockEntity)

    @Delete
    @Throws(Exception::class)
    fun delete(myStockEntity: MyStockEntity)

    //Query
    @Query("SELECT * FROM my_stock")
    @Throws(Exception::class)
    fun getAll(): MutableList<MyStockEntity>

    @Query("SELECT * FROM my_stock WHERE subjectName = :subjectName")
    @Throws(Exception::class)
    fun getSubjectName(subjectName: String): MyStockEntity
}