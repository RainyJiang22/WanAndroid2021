package com.base.wanandroid.ui.share

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.base.wanandroid.bean.NoDataResponse
import com.base.wanandroid.bean.ShareListResponse
import com.base.wanandroid.network.RetrofitHelper
import io.reactivex.Observable
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * @author jiangshiyu
 * @date 2022/6/16
 */
class ShareViewModel : BaseViewModel() {


    /**
     * 获取分享列表
     */
    fun getShareList(page: Int): Observable<ShareListResponse> {
        return RetrofitHelper.get().shareList(page)
    }


    /**
     * 分享当前文章
     */
    fun shareCurrentArticle(title: String, link: String): Observable<NoDataResponse> {
        return RetrofitHelper.get().shareArticle(title, link)
    }

    /**
     * 删除
     */
    fun deleteSharedArticle(id: Int): Observable<NoDataResponse> {
        return RetrofitHelper.get().deleteShareArticle(id)
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