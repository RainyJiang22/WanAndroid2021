package com.base.wanandroid.mvi.repository

import com.base.wanandroid.bean.ArticleResponse
import com.base.wanandroid.bean.base.ApiPagerResponse
import com.base.wanandroid.mvi.WanApi
import com.base.wanandroid.utils.PageState
import kotlinx.coroutines.delay

/**
 * @author jiangshiyu
 * @date 2022/6/10
 */
class ArticleRepository {

    companion object {
        //单例
        @Volatile
        private var instance: ArticleRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: ArticleRepository().also { instance = it }
            }
    }

    suspend fun getArticleList(page: Int): PageState<ApiPagerResponse<ArticleResponse>> {
        val articleListResult = try {
            delay(2000)
            WanApi.create().getArticleList(page)
        } catch (e: Exception) {
            return PageState.Error(e)
        }
        articleListResult.data.let {
            return PageState.Success(data = it)
        }
    }

}