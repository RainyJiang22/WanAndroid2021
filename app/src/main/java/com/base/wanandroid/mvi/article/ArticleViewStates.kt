package com.base.wanandroid.mvi.article

import com.base.wanandroid.bean.ArticleResponse
import com.base.wanandroid.utils.FetchStatus

/**
 * @author jiangshiyu
 * @date 2022/6/10
 */

data class ArticleViewState(
    val fetchStatus: FetchStatus = FetchStatus.NotFetched,
    val articleList: RefreshData = RefreshData()
)

data class RefreshData(
    val currentList: List<ArticleResponse> = emptyList(),
    val isRefresh: Boolean = false
)


//事件
sealed class ArticleViewEvent {

    data class ShowToast(val message: String) : ArticleViewEvent()
}

//行为
sealed class ArticleViewAction {

    data class ArticleItemClicked(val articleItem: ArticleResponse) : ArticleViewAction()
    //上拉刷新
    object OnSwipeRefresh : ArticleViewAction()
    //下拉加载
    object OnLoadMore : ArticleViewAction()
    //解析
    object FetchArticle : ArticleViewAction()
}
