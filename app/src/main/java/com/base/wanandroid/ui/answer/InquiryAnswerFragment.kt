package com.base.wanandroid.ui.answer

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.nekocode.rxlifecycle.LifecycleEvent
import cn.nekocode.rxlifecycle.compact.RxLifecycleCompact
import com.base.wanandroid.base.BaseFragment
import com.base.wanandroid.databinding.FragmentChildBinding
import com.base.wanandroid.ui.adapter.ArticleAdapter
import com.base.wanandroid.ui.home.ArticleDiffCallBack
import com.base.wanandroid.utils.RxTransformer
import com.base.wanandroid.utils.lifecycleOwner
import com.drake.brv.PageRefreshLayout
import kotlinx.coroutines.launch

/**
 * @author jiangshiyu
 * @date 2022/6/6
 */
class InquiryAnswerFragment : BaseFragment<FragmentChildBinding, AnswerViewModel>() {


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
            lifecycleScope.launch {
                viewModel.getAnswerList(index)
                    .compose(
                        RxLifecycleCompact.bind(this@InquiryAnswerFragment)
                            .disposeObservableWhen(LifecycleEvent.DESTROY_VIEW)
                    )
                    .compose(RxTransformer.async())
                    .subscribe({
                        if (first && it.data.datas.isEmpty()) {
                            showEmpty()
                        } else {
                            first = false
                            index += if (index == 1) {
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
                        }
                    }, {
                        showError()
                    }).lifecycleOwner(this@InquiryAnswerFragment)

            }
            showContent(true)
        }?.autoRefresh()
    }

    override fun onResume() {
        super.onResume()
        PageRefreshLayout.startIndex = 1
    }
}