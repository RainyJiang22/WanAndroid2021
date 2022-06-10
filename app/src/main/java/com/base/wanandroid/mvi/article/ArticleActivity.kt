package com.base.wanandroid.mvi.article

import android.os.Bundle
import com.base.mvi_core.observeState
import com.base.wanandroid.base.BaseActivity
import com.base.wanandroid.databinding.ArticleLayoutBinding
import com.base.wanandroid.ui.adapter.ArticleAdapter
import com.base.wanandroid.ui.home.ArticleDiffCallBack
import com.base.wanandroid.utils.FetchStatus

/**
 * @author jiangshiyu
 * @date 2022/6/10
 */
class ArticleActivity : BaseActivity<ArticleLayoutBinding, ArticleViewModel>() {

    private val articleAdapter by lazy {
        ArticleAdapter(true).apply {
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
            viewModel.dispatch(ArticleViewAction.OnSwipeRefresh)
        }


    }

    private fun initViewModel() {
        viewModel.viewStates.run {
            observeState(this@ArticleActivity, ArticleViewState::articleList) {
                articleAdapter.setDiffNewData(it.toMutableList())
            }
            observeState(this@ArticleActivity, ArticleViewState::fetchStatus) {
                when (it) {
                    is FetchStatus.Fetched -> {
                        binding?.srlArticleHome?.isRefreshing = false
                    }
                    is FetchStatus.NotFetched -> {
                        binding?.srlArticleHome?.isRefreshing = false
                        viewModel.dispatch(ArticleViewAction.FetchArticle)
                    }
                    is FetchStatus.Fetching -> {
                        binding?.srlArticleHome?.isRefreshing = true
                    }
                }
            }
        }
    }
}