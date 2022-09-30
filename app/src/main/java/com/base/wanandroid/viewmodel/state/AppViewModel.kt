package com.base.wanandroid.viewmodel.state

import com.base.wanandroid.data.UserInfo
import com.base.wanandroid.utils.CacheUtil
import com.kunminx.architecture.ui.callback.UnPeekLiveData
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * @author jiangshiyu
 * @date 2022/9/29
 *
 * app全局viewModel,用于替代传统的eventbus，liveEventbus
 */
class AppViewModel : BaseViewModel() {

    //App的账户信息
    var userInfo = UnPeekLiveData.Builder<UserInfo>().setAllowNullValue(true).create()


    init {
        //默认存放的app账户信息
        userInfo.value = CacheUtil.getUser()
    }
}