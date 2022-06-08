package com.base.wanandroid.history.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.base.wanandroid.history.bean.HistoryEntity
import io.reactivex.Observable

/**
 * @author jiangshiyu
 * @date 2022/6/8
 */

@Dao
interface HistoryDao {


    /**
     * 查询历史列表
     */
    @Query("SELECT * FROM wan_history")
    fun historyAsObservable(): Observable<List<HistoryEntity>>

    /**
     * 插入或更新
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(entity: HistoryEntity)

    /**
     * 插入或更新
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateList(entity: List<HistoryEntity>)


    /**
     * 清除所有数据
     */
    @Query("DELETE FROM wan_history")
    fun cleanTable()

    @Delete
    fun deleteHistory(entity: HistoryEntity)
}