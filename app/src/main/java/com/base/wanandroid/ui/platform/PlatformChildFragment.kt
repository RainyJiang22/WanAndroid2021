package com.base.wanandroid.ui.platform

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
 * @date 2022/5/31
 */
class PlatformChildFragment : BaseFragment<FragmentChildBinding, PlatformViewModel>() {


    companion object {

        fun newInstance(cid: Int): PlatformChildFragment {
            val args = Bundle().apply {
                putInt("cid", cid)
            }
            val fragment = PlatformChildFragment()
            fragment.arguments = args
            return fragment
        }

    }

    private val articleAdapter by lazy {
        ArticleAdapter(true).apply {
            this.setDiffCallback(ArticleDiffCallBack())
        }
    }

    private var cid = 0

    override fun onBundle(bundle: Bundle) {
    }

    override fun init(savedInstanceState: Bundle?) {

        arguments?.let {
            //项目id
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
            lifecycleScope.launch {
                viewModel.getPlatFormHistory(cid, index)
                    .compose(
                        RxLifecycleCompact.bind(this@PlatformChildFragment)
                            .disposeObservableWhen(LifecycleEvent.DESTROY_VIEW)
                    )
                    .compose(RxTransformer.async())
                    .subscribe({
                        if (it.data.datas.isEmpty()) {
                            showEmpty()
                        } else {
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
                    }).lifecycleOwner(this@PlatformChildFragment)

            }
            showContent(true)
        }?.autoRefresh()
    }


    override fun onResume() {
        super.onResume()
        PageRefreshLayout.startIndex = 1
    }
}