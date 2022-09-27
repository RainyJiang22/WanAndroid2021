package com.base.wanandroid.viewmodel.state

import com.base.wanandroid.bean.NoDataResponse
import com.base.wanandroid.network.RetrofitHelper
import io.reactivex.Observable
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * @author jiangshiyu
 * @date 2022/9/27
 */
class HomeViewModel : BaseViewModel() {


    /**
     * 退出
     */
    fun loginOut(): Observable<NoDataResponse> {
        return RetrofitHelper.get().logout()
    }
}