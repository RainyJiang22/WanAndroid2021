package com.base.wanandroid.ui.collect

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.base.wanandroid.bean.CollectListResponse
import com.base.wanandroid.bean.NoDataResponse
import com.base.wanandroid.network.RetrofitHelper
import io.reactivex.Observable

/**
 * @author jiangshiyu
 * @date 2022/6/14
 */
class CollectViewModel(application: Application) : AndroidViewModel(application) {


    /**
     * 登录后操作
     */
    fun collectList(userName: String, password: String, page: Int): Observable<CollectData> {
        return RetrofitHelper.get().loginWanAndroid(userName, password)
            .flatMap { user ->
                getCollectList(page).map {
                    CollectData(user.data, it.data)
                }
            }
    }

    /**
     * 收藏列表
     */
    fun getCollectList(page: Int): Observable<CollectListResponse> {
        return RetrofitHelper.get().getCollectList(page)
    }

    /**
     * 收藏当前文章
     */
    fun collectCurrentArticle(articleId: Int): Observable<NoDataResponse> {
        return RetrofitHelper.get().collectCurrentArticle(articleId)
    }

    /**
     * 取消收藏当前文章
     */
    fun unCollectArticle(articleId: Int, originId: Int = -1): Observable<NoDataResponse> {
        return RetrofitHelper.get().unCollectArticle(articleId, originId)
    }

    /**
     * 收藏界面取消收藏
     */
    fun userUnCollectArticle(articleId: Int, originId: Int = -1): Observable<NoDataResponse> {
        return RetrofitHelper.get().userUnCollectArticle(articleId, originId)
    }


}