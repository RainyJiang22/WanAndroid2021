package com.base.wanandroid.mvi.article

import android.os.Bundle
import com.base.mvi_core.observeEvent
import com.base.mvi_core.observeState
import com.base.wanandroid.base.BaseActivity
import com.base.wanandroid.databinding.ArticleLayoutBinding
import com.base.wanandroid.ui.adapter.ArticleAdapter
import com.base.wanandroid.ui.home.ArticleDiffCallBack
import com.base.wanandroid.utils.FetchStatus
import com.blankj.utilcode.util.ToastUtils

/**
 * @author jiangshiyu
 * @date 2022/6/10
 */
class ArticleActivity : BaseActivity<ArticleLayoutBinding, ArticleViewModel>() {


    private val articleAdapter by lazy {
        ArticleAdapter(this, null,true).apply {
            this.setDiffCallback(ArticleDiffCallBack())
        }
    }

    override fun onBundle(bundle: Bundle) {

    }

    override fun init(savedInstanceState: Bundle?) {
        initView()
        initViewModel()
    }

    private fun initView() {
        binding?.rvArticleHome?.adapter = articleAdapter

        binding?.srlArticleHome?.setOnRefreshListener {
            it.finishRefresh(2000)
            viewModel.dispatch(ArticleViewAction.OnSwipeRefresh)
        }

        binding?.srlArticleHome?.setOnLoadMoreListener {
            it.finishLoadMore(2000)
            viewModel.dispatch(ArticleViewAction.OnSwipeRefresh)
        }


    }

    private fun initViewModel() {
        viewModel.viewStates.run {
            observeState(this@ArticleActivity, ArticleViewState::articleList) {
                if (it.isRefresh) {
                    articleAdapter.addData(it.currentList)
                } else {
                    articleAdapter.setList(it.currentList)
                }
            }
            observeState(this@ArticleActivity, ArticleViewState::fetchStatus) {
                when (it) {
                    is FetchStatus.Fetched -> {
                    }
                    is FetchStatus.NotFetched -> {
                        //第一次刷新
                        binding?.srlArticleHome?.autoRefresh()
                        viewModel.dispatch(ArticleViewAction.FetchArticle)
                    }
                    is FetchStatus.Fetching -> {
                    }
                }
            }
        }

        viewModel.viewEvents.observeEvent(this) {
            renderViewEvent(it)
        }
    }


    private fun renderViewEvent(viewEvent: ArticleViewEvent) {
        when (viewEvent) {
            is ArticleViewEvent.ShowToast -> {
                ToastUtils.showShort(viewEvent.message)
            }
        }
    }
}