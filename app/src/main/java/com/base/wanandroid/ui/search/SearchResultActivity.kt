package com.base.wanandroid.ui.search

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import cn.nekocode.rxlifecycle.LifecycleEvent
import cn.nekocode.rxlifecycle.compact.RxLifecycleCompact
import com.base.wanandroid.base.BaseActivity
import com.base.wanandroid.databinding.ActivitySearchResultBinding
import com.base.wanandroid.ui.adapter.ArticleAdapter
import com.base.wanandroid.ui.collect.ArticleViewModel
import com.base.wanandroid.ui.home.ArticleDiffCallBack
import com.base.wanandroid.utils.RxTransformer
import com.base.wanandroid.utils.initFloatBtn
import com.base.wanandroid.utils.lifecycleOwner
import com.drake.brv.PageRefreshLayout
import com.drake.serialize.intent.bundle
import kotlinx.coroutines.launch

/**
 * @author jiangshiyu
 * @date 2022/6/16
 */
class SearchResultActivity : BaseActivity<ActivitySearchResultBinding, ArticleViewModel>() {


    private val articleAdapter by lazy {
        ArticleAdapter(this, true).apply {
            this.setDiffCallback(ArticleDiffCallBack())
        }
    }

    private var first = true
    private val key: String by bundle()

    override fun onBundle(bundle: Bundle) {
    }

    override fun init(savedInstanceState: Bundle?) {
        PageRefreshLayout.startIndex = 0
        binding?.titleBar?.title = key
        //标题栏返回按钮关闭页面
        binding?.titleBar?.leftView?.setOnClickListener { finish() }
        //设置RecycleView的Adapter
        binding?.child?.rv?.apply {
            adapter = articleAdapter
            binding?.fab?.let { initFloatBtn(it) }
        }
        onRefresh()
    }


    private fun onRefresh() {
        binding?.child?.page?.onRefresh {
            lifecycleScope.launch {
                viewModel.getSearchResult(index, key)
                    .compose(
                        RxLifecycleCompact.bind(this@SearchResultActivity)
                            .disposeObservableWhen(LifecycleEvent.DESTROY)
                    )
                    .compose(RxTransformer.async())
                    .subscribe({
                        if (first && it.data.datas.isEmpty()) {
                            showEmpty()
                        } else {
                            first = false
                            index += if (index == 0) {
                                articleAdapter.setList(it.data.datas)
                                1
                            } else {
                                if (it.data.datas.isNullOrEmpty()) {
                                    showContent(false)
                                } else {
                                    articleAdapter.addData(it.data.datas)
                                }
                                1
                            }
                            showContent(true)
                        }
                    }, {
                        showError()
                    }).lifecycleOwner(this@SearchResultActivity)

            }
        }?.autoRefresh()

    }
}