package com.yjpapp.stockportfolio.localdb.room.userinfo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_info")
data class UserInfoEntity (
    @PrimaryKey(autoGenerate = false)
    var userIndex: Int = 0,
    @ColumnInfo(name = "email")
    var email: String?,
    @ColumnInfo(name = "name")
    var name: String?,
    @ColumnInfo(name = "login_type")
    var login_type: String?,
    @ColumnInfo(name = "token")
    var token: String?
    )