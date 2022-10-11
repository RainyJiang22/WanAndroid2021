package com.base.wanandroid.viewmodel.state

import com.base.wanandroid.ui.collect.CollectBus
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.event.EventLiveData

/**
 * @author jiangshiyu
 * @date 2022/10/4
 */
class EventViewModel : BaseViewModel() {

    //全局收藏
    val collectEvent = EventLiveData<CollectBus>()

    //分享文章通知
    val shareArticleEvent = EventLiveData<Boolean>()
}