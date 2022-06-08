package com.base.wanandroid.history.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author jiangshiyu
 * @date 2022/6/8
 */
@Entity(tableName = "wan_history")
data class HistoryEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long?,

    @ColumnInfo(name = "history_title", defaultValue = "未知")
    val title: String,

    @ColumnInfo(name = "history_url")
    val url: String,

    @ColumnInfo(name = "history_date")
    val date: String


)