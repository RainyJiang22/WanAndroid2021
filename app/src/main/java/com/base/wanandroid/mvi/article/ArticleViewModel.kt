package com.base.wanandroid.mvi.article

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.mvi_core.LiveEvents
import com.base.mvi_core.setEvent
import com.base.mvi_core.setState
import com.base.wanandroid.bean.ArticleResponse
import com.base.wanandroid.mvi.repository.ArticleRepository
import com.base.wanandroid.utils.FetchStatus
import com.base.wanandroid.utils.PageState
import com.base.wanandroid.utils.asLiveData
import kotlinx.coroutines.launch

/**
 * @author jiangshiyu
 * @date 2022/6/10
 */
class ArticleViewModel(application: Application) : AndroidViewModel(application) {

    //页数
    private var page = 0

    private val repository: ArticleRepository = ArticleRepository.getInstance()
    private val _viewStates: MutableLiveData<ArticleViewState> = MutableLiveData(ArticleViewState())
    private val _viewEvents: LiveEvents<ArticleViewEvent> = LiveEvents()
    val viewStates = _viewStates.asLiveData()
    val viewEvents = _viewEvents.asLiveData()


    fun dispatch(viewAction: ArticleViewAction) {
        when (viewAction) {
            is ArticleViewAction.ArticleItemClicked -> articleItemClicked(viewAction.articleItem)
            ArticleViewAction.OnSwipeRefresh -> fetchArticleList(isRefresh = false)
            ArticleViewAction.OnLoadMore -> fetchArticleList(isRefresh = true)
            ArticleViewAction.FetchArticle -> fetchArticleList(isRefresh = false)
        }
    }


    private fun articleItemClicked(articleItem: ArticleResponse) {
        _viewEvents.setEvent(ArticleViewEvent.ShowToast(message = articleItem.title))
    }

    private fun fetchArticleList(isRefresh: Boolean) {
        _viewStates.setState {
            //正在加载
            copy(fetchStatus = FetchStatus.Fetching)
        }

        viewModelScope.launch {
            when (val result = repository.getArticleList(page)) {
                is PageState.Error -> {

                    //加载失败
                    _viewStates.setState {
                        copy(fetchStatus = FetchStatus.Fetched)
                    }
                    _viewEvents.setEvent(ArticleViewEvent.ShowToast(message = result.message))
                }

                is PageState.Success -> {
                    //加载成功
                    page++
                    _viewStates.setState {
                        copy(
                            fetchStatus = FetchStatus.Fetched,
                            articleList = RefreshData(result.data.datas, isRefresh)
                        )
                    }
                }
            }
        }
    }


}