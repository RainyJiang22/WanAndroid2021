package com.base.wanandroid.ui.answer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.base.wanandroid.bean.ArticleListResponse
import com.base.wanandroid.bean.NoDataResponse
import com.base.wanandroid.network.RetrofitHelper
import io.reactivex.Observable

/**
 * @author jiangshiyu
 * @date 2022/6/6
 */
class AnswerViewModel(application: Application) : AndroidViewModel(application) {


    /**
     * 获取问答列表
     */
    fun getAnswerList(page: Int): Observable<ArticleListResponse> {
        return RetrofitHelper.get().getAnswerList(page)
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
}