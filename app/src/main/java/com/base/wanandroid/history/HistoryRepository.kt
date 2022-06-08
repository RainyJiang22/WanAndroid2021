package com.base.wanandroid.history

import com.base.wanandroid.db.WanAndroidDB
import com.base.wanandroid.history.bean.HistoryEntity
import io.reactivex.Completable
import io.reactivex.Observable

/**
 * @author jiangshiyu
 * @date 2022/6/8
 */
object HistoryRepository {


    /**
     * 获取历史记录列表
     */
    fun historyList(): Observable<List<HistoryEntity>> {
        return WanAndroidDB
            .getInstance()
            .historyDao()
            .historyAsObservable()
    }

    /**
     * 添加历史记录
     */
    fun saveHistoryRecord(historyEntity: HistoryEntity): Completable {
        return Completable.create {
            WanAndroidDB.getInstance()
                .historyDao()
                .insertOrUpdate(historyEntity)
            it.onComplete()
        }
    }

    /**
     * 添加历史记录列表
     */
    fun saveHistoryRecordList(list: List<HistoryEntity>): Completable {
        return Completable.create {
            WanAndroidDB.getInstance()
                .historyDao()
                .insertOrUpdateList(list)
            it.onComplete()
        }
    }

    /**
     * 删除单条历史记录
     */
    fun deleteHistoryRecord(historyEntity: HistoryEntity): Completable {
        return Completable.create {
            WanAndroidDB.getInstance()
                .historyDao()
                .deleteHistory(historyEntity)
            it.onComplete()
        }
    }

    /**
     * 删除全部历史记录
     */
    fun deleteAllHistoryRecord(): Completable {
        return Completable.create {
            WanAndroidDB.getInstance()
                .historyDao()
                .cleanTable()
            it.onComplete()
        }
    }

}