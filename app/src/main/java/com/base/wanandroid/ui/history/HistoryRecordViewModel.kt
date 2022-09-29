package com.base.wanandroid.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.base.wanandroid.history.HistoryRepository
import com.base.wanandroid.history.bean.HistoryEntity
import io.reactivex.Observable
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * @author jiangshiyu
 * @date 2022/6/9
 */
class HistoryRecordViewModel : BaseViewModel() {


    /**
     * 历史记录列表
     */
    val historyRecordList: Observable<List<HistoryEntity>> by lazy {
        HistoryRepository.historyList()
    }
}