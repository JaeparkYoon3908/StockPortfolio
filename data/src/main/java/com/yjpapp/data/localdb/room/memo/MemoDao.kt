package com.yjpapp.data.localdb.room.memo

import androidx.room.*
import kotlin.Exception
import kotlin.jvm.Throws

/**
 * @author 윤재박
 * @since 2022.01.06
 */

@Dao
interface MemoDao {
    @Insert
    @Throws(Exception::class)
    fun insert(memoListEntity: MemoListEntity)

    @Update
    @Throws(Exception::class)
    fun update(memoListEntity: MemoListEntity)

    @Query("UPDATE memo_list SET deleteChecked = :updateChecked WHERE id = :id")
    @Throws(Exception::class)
    fun updateDeleteChecked(id: Int, updateChecked: String)

    @Delete
    @Throws(Exception::class)
    fun delete(memoListEntity: MemoListEntity)

    @Query("DELETE FROM memo_list WHERE id = :id")
    @Throws(Exception::class)
    fun deleteMemoInfo(id: Int)

    @Query("SELECT * FROM memo_list")
    @Throws(Exception::class)
    fun getAll(): MutableList<MemoListEntity>

    @Query("SELECT * FROM memo_list WHERE id = :id")
    @Throws(Exception::class)
    fun getMemoInfo(id: Int): MemoListEntity
}