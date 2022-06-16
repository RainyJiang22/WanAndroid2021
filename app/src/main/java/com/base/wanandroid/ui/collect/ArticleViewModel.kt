package com.base.wanandroid.ui.collect

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.base.wanandroid.bean.ArticleListResponse
import com.base.wanandroid.bean.BannerResponse
import com.base.wanandroid.bean.ClassificationListResponse
import com.base.wanandroid.bean.HomeListResponse
import com.base.wanandroid.bean.NoDataResponse
import com.base.wanandroid.bean.TreeListResponse
import com.base.wanandroid.network.RetrofitHelper
import io.reactivex.Observable

/**
 * @author jiangshiyu
 * @date 2022/6/15
 */
class ArticleViewModel(application: Application) : AndroidViewModel(application) {

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


    /**
     * 退出
     */
    fun loginOut(): Observable<NoDataResponse> {
        return RetrofitHelper.get().logout()
    }

    /**
     * 公众号列表数据
     */
    fun getPlatFormList(): Observable<ClassificationListResponse> {
        return RetrofitHelper.get().getPlatformList()
    }

    /**
     * 公众号历史数据
     */
    fun getPlatFormHistory(cid: Int, page: Int): Observable<ArticleListResponse> {
        return RetrofitHelper.get().getPlatformHistory(cid, page)
    }

    /**
     * 获取问答列表
     */
    fun getAnswerList(page: Int): Observable<ArticleListResponse> {
        return RetrofitHelper.get().getAnswerList(page)
    }

    /**
     * 获取项目分类数据
     */
    fun getProjectType(): Observable<ClassificationListResponse> {
        return RetrofitHelper.get().getProjectType()
    }

    /**
     * 获取项目列表数据
     */
    fun getProjectList(page: Int, cid: Int): Observable<ArticleListResponse> {
        return RetrofitHelper.get().getProjectList(page, cid)
    }

    /**
     * 获取项目最新数据
     */
    fun getNewProject(page: Int): Observable<ArticleListResponse> {
        return RetrofitHelper.get().getProjectNew(page)
    }

    /**
     * 获取广场列表数据
     */
    fun getSquareList(page: Int): Observable<ArticleListResponse> {
        return RetrofitHelper.get().getSquareList(page)
    }

    /**
     * 体系列表
     */
    fun getTreeList(): Observable<TreeListResponse> {
        return RetrofitHelper.get().getTypeTreeList()
    }


    /**
     * 知识体系下的文章
     */
    fun getTreeArticleList(cid: Int, page: Int): Observable<ArticleListResponse> {
        return RetrofitHelper.get().getTreeArticleList(page, cid)
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
     * 搜索结果
     */
    fun getSearchResult(page: Int, key: String): Observable<ArticleListResponse> {
        return RetrofitHelper.get().getSearchList(page, key)
    }
}