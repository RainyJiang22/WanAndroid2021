package com.base.wanandroid.ui.square

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.nekocode.rxlifecycle.LifecycleEvent
import cn.nekocode.rxlifecycle.compact.RxLifecycleCompact
import com.base.wanandroid.base.BaseFragment
import com.base.wanandroid.bean.ArticleListResponse
import com.base.wanandroid.databinding.FragmentChildBinding
import com.base.wanandroid.databinding.FragmentSquareBinding
import com.base.wanandroid.ui.adapter.ArticleAdapter
import com.base.wanandroid.ui.home.ArticleDiffCallBack
import com.base.wanandroid.utils.RxTransformer
import com.base.wanandroid.utils.lifecycleOwner

/**
 * @author jiangshiyu
 * @date 2022/5/31
 * 广场fragment
 */
class SquareChildFragment : BaseFragment<FragmentChildBinding, SquareViewModel>() {


    private var first = true

    private val articleAdapter by lazy {
        ArticleAdapter(true).apply {
            this.setDiffCallback(ArticleDiffCallBack())
        }
    }

    override fun onBundle(bundle: Bundle) {

    }

    override fun init(savedInstanceState: Bundle?) {
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

            loadSquareList(index, {
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
                            return@loadSquareList
                        }
                        articleAdapter.addData(it.data.datas)
                        1
                    }
                    showContent(true)
                }
            }, {
                showError()
            })

        }?.autoRefresh()
    }

    /**
     * 加载广场列表数据
     */
    private fun loadSquareList(
        page: Int,
        onNext: (data: ArticleListResponse) -> Unit,
        onError: () -> Unit
    ) {
        viewModel.getSquareList(page)
            .compose(
                RxLifecycleCompact.bind(this).disposeObservableWhen(
                    LifecycleEvent.DESTROY_VIEW
                )
            )
            .compose(RxTransformer.async())
            .subscribe({
                onNext(it)
            }, {
                onError()
            }).lifecycleOwner(this)
    }
}