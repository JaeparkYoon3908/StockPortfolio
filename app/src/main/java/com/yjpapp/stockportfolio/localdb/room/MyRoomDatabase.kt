package com.yjpapp.stockportfolio.localdb.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.yjpapp.stockportfolio.localdb.room.memo.MemoListDao
import com.yjpapp.stockportfolio.localdb.room.memo.MemoListEntity
import com.yjpapp.stockportfolio.localdb.room.mystock.MyStockDao
import com.yjpapp.stockportfolio.localdb.room.mystock.MyStockEntity
import javax.inject.Singleton

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
        private var instance: MyRoomDatabase? = null

        fun getInstance(context: Context): MyRoomDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context)
            }
        }

        private  fun buildDatabase(context: Context): MyRoomDatabase {
            return Room.databaseBuilder(context.applicationContext, MyRoomDatabase::class.java, DB_NAME)
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                    }})
                    .allowMainThreadQueries() //추천하진 않지만 MainThread(UI Thread)에서 접근 가능할 수 있게 설정
                    .fallbackToDestructiveMigration() //room table 업데이트 시 마이그레이션
                    .build()
        }
    }
}