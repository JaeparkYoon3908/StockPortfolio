package com.yjpapp.stockportfolio.localdb.room.memo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author 윤재박
 * @since 2022.01.06
 */
@Entity(tableName = "memo_list")
data class MemoListEntity (
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "date") var date: String = "",
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "content") var content: String = "",
    @ColumnInfo(name = "deleteChecked") var deleteChecked: String = ""
)