package com.base.wanandroid.ui.tree

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.nekocode.rxlifecycle.LifecycleEvent
import cn.nekocode.rxlifecycle.compact.RxLifecycleCompact
import com.base.wanandroid.base.BaseFragment
import com.base.wanandroid.databinding.FragmentChildBinding
import com.base.wanandroid.ui.adapter.ArticleAdapter
import com.base.wanandroid.ui.collect.ArticleViewModel
import com.base.wanandroid.ui.home.ArticleDiffCallBack
import com.base.wanandroid.utils.RxTransformer
import com.base.wanandroid.utils.lifecycleOwner
import com.drake.brv.PageRefreshLayout

/**
 * @author jiangshiyu
 * @date 2022/6/2
 */
class TreeChildFragment : BaseFragment<FragmentChildBinding, ArticleViewModel>() {


    companion object {
        fun newInstance(cid: Int): TreeChildFragment {
            val args = Bundle().apply {
                putInt("cid", cid)
            }
            val fragment = TreeChildFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var cid = 0
    private var first = true


    private val articleAdapter by lazy {
        ArticleAdapter(this, viewModel, true).apply {
            this.setDiffCallback(ArticleDiffCallBack())
        }
    }

    override fun onBundle(bundle: Bundle) {

    }

    override fun init(savedInstanceState: Bundle?) {
        arguments?.let {
            cid = it.getInt("cid")
        }
        initAdapter()
    }

    private fun initAdapter() {
        binding?.rv?.also {
            it.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            it.adapter = articleAdapter
        }
        onRefresh()
    }

    private fun onRefresh() {

        binding?.page?.onRefresh {
            viewModel.getTreeArticleList(cid, index)
                .compose(
                    RxLifecycleCompact.bind(this@TreeChildFragment)
                        .disposeObservableWhen(LifecycleEvent.DESTROY_VIEW)
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
                                return@subscribe
                            }
                            articleAdapter.addData(it.data.datas)
                            1
                        }
                        showContent(true)
                    }
                }, {
                    showError()
                }).lifecycleOwner(this@TreeChildFragment)
        }?.autoRefresh()

    }

    override fun onResume() {
        super.onResume()
        PageRefreshLayout.startIndex = 0
    }
}