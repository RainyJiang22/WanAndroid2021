package com.base.wanandroid.ui.navigation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.base.wanandroid.bean.NavigationListResponse
import com.base.wanandroid.network.RetrofitHelper
import io.reactivex.Observable

/**
 * @author jiangshiyu
 * @date 2022/6/6
 */
class NavigationViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * 获取导航数据
     */
    fun getNavigationData(): Observable<NavigationListResponse> {
        return RetrofitHelper.get().getNaviList()
    }


}