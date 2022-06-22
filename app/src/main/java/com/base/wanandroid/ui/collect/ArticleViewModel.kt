package com.base.wanandroid.ui.collect

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.base.wanandroid.bean.ArticleListResponse
import com.base.wanandroid.bean.BannerResponse
import com.base.wanandroid.bean.ClassificationListResponse
import com.base.wanandroid.bean.NoDataResponse
import com.base.wanandroid.bean.ShareListResponse
import com.base.wanandroid.bean.TreeListResponse
import com.base.wanandroid.network.RetrofitHelper
import com.base.wanandroid.utils.RxCacheManager
import com.base.wanandroid.utils.TimeConstant
import com.zchu.rxcache.data.CacheResult
import com.zchu.rxcache.kotlin.rxCache
import com.zchu.rxcache.stategy.CacheStrategy
import io.reactivex.Observable

/**
 * @author jiangshiyu
 * @date 2022/6/15
 */
class ArticleViewModel(application: Application) : AndroidViewModel(application) {


    companion object {
        const val ARTICLE_DATA = "article_data"
        const val PLATFORM_HISTORY = "platform_history"
        const val PROJECT_LIST = "project_list"
        const val ANSWER_LIST = "answer_list"
        const val NEW_PROJECT = "new_project"
        const val TREE_LIST = "tree_list"
    }

    /**
     * 获取主页文章列表数据
     */
    fun getArticleList(page: Int): Observable<CacheResult<ArticleListResponse>> {
        return RetrofitHelper.get().getArticleList(page)
            .rxCache(
                RxCacheManager.rxCache,
                ARTICLE_DATA,
                CacheStrategy.firstCacheTimeout((TimeConstant.ONE_DAY))
            )
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
    fun getPlatFormHistory(cid: Int, page: Int): Observable<CacheResult<ArticleListResponse>> {
        return RetrofitHelper.get().getPlatformHistory(cid, page)
            .rxCache(
                RxCacheManager.rxCache,
                PLATFORM_HISTORY,
                CacheStrategy.firstCacheTimeout((TimeConstant.ONE_DAY))
            )
    }

    /**
     * 获取问答列表
     */
    fun getAnswerList(page: Int): Observable<CacheResult<ArticleListResponse>> {
        return RetrofitHelper.get().getAnswerList(page)
            .rxCache(
                RxCacheManager.rxCache,
                ANSWER_LIST,
                CacheStrategy.firstCacheTimeout((TimeConstant.ONE_DAY))
            )
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
    fun getProjectList(page: Int, cid: Int): Observable<CacheResult<ArticleListResponse>> {
        return RetrofitHelper.get().getProjectList(page, cid)
            .rxCache(
                RxCacheManager.rxCache,
                PROJECT_LIST,
                CacheStrategy.firstCacheTimeout((TimeConstant.ONE_DAY))
            )
    }

    /**
     * 获取项目最新数据
     */
    fun getNewProject(page: Int): Observable<CacheResult<ArticleListResponse>> {
        return RetrofitHelper.get().getProjectNew(page)
            .rxCache(
                RxCacheManager.rxCache,
                NEW_PROJECT,
                CacheStrategy.firstCacheTimeout((TimeConstant.ONE_DAY))
            )

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
    fun getTreeArticleList(cid: Int, page: Int): Observable<CacheResult<ArticleListResponse>> {
        return RetrofitHelper.get().getTreeArticleList(page, cid)
            .rxCache(
                RxCacheManager.rxCache,
                TREE_LIST,
                CacheStrategy.firstCacheTimeout((TimeConstant.ONE_DAY))
            )
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


    /**
     * 根据作者名称查询
     */
    fun searchAuthorByName(page: Int, name: String): Observable<ArticleListResponse> {
        return RetrofitHelper.get().searchArticleByName(page, name)
    }

    fun searchAuthorById(userId: Int, page: Int): Observable<ShareListResponse> {
        return RetrofitHelper.get().searchArticleById(page, userId)
    }
}