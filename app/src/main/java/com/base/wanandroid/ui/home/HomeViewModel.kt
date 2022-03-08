package com.base.wanandroid.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.base.wanandroid.bean.BannerResponse
import com.base.wanandroid.bean.HomeListResponse
import com.base.wanandroid.network.RetrofitHelper
import io.reactivex.Observable

/**
 * @author jiangshiyu
 * @date 2022/3/7
 */
class HomeViewModel(application: Application) : AndroidViewModel(application) {


    /**
     * 获取主页数据
     */
    fun getHomeList(page: Int): Observable<HomeListResponse> {
        return RetrofitHelper.get().getHomeList(page)

    }


    /**
     * 获取banner数据
     */
    fun getBannerData(): Observable<BannerResponse> {
        return RetrofitHelper.get().getBanner()
    }
}