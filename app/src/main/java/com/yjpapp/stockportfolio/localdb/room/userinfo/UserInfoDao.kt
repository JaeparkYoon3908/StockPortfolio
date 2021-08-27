package com.yjpapp.stockportfolio.localdb.room.userinfo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

@Dao
interface UserInfoDao {
    @Insert
    fun insert(userInfoEntity: UserInfoEntity)

    @Update
    fun update(userInfoEntity: UserInfoEntity)

    @Delete
    fun delete(userIndex: Int)

    //Query
}