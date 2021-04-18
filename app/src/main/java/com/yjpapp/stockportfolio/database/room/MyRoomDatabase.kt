package com.yjpapp.stockportfolio.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [MyStockEntity::class], version = 1, exportSchema = false)
abstract class MyRoomDatabase: RoomDatabase() {
    //TODO DB를 저장, 수정, 삭제를 했을 때 예외처리는 어떻게 하는지 구현하기
    abstract fun myStockDao(): MyStockDao

    companion object {
        private val DB_NAME = "room-db"
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
                    .build()
        }
    }
}