package com.base.wanandroid.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.base.wanandroid.history.HistoryRepository
import com.base.wanandroid.history.bean.HistoryEntity
import io.reactivex.Observable

/**
 * @author jiangshiyu
 * @date 2022/6/9
 */
class HistoryRecordViewModel(application: Application) : AndroidViewModel(application) {


    /**
     * 历史记录列表
     */
    val historyRecordList: Observable<List<HistoryEntity>> by lazy {
        HistoryRepository.historyList()
    }
}