package com.base.wanandroid.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.base.wanandroid.bean.ArticleListResponse
import com.base.wanandroid.bean.BannerResponse
import com.base.wanandroid.network.RetrofitHelper
import io.reactivex.Observable

/**
 * @author jiangshiyu
 * @date 2022/3/7
 */
class HomeViewModel(application: Application) : AndroidViewModel(application) {


    /**
     * 获取主页文章列表数据
     */
    fun getArticleList(page: Int): Observable<ArticleListResponse> {
        return RetrofitHelper.get().getArticleList(page)
    }


    /**
     * 获取banner数据
     */
    fun getBannerData(): Observable<BannerResponse> {
        return RetrofitHelper.get().getBanner()
    }
}