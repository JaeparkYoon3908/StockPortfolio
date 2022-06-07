package com.yjpapp.data.localdb.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yjpapp.data.localdb.room.memo.MemoListDao
import com.yjpapp.data.localdb.room.memo.MemoListEntity
import com.yjpapp.data.localdb.room.mystock.MyStockDao
import com.yjpapp.data.localdb.room.mystock.MyStockEntity

@Database(
    entities = [MyStockEntity::class, MemoListEntity::class],
    version = 3,
    exportSchema = false
)

abstract class MyRoomDatabase: RoomDatabase() {
    abstract fun myStockDao(): MyStockDao
    abstract fun memoListDao(): MemoListDao

    companion object {
        val DB_NAME = "room-db"
    }
}