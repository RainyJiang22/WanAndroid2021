package com.base.wanandroid.ui.tree

import android.os.Bundle
import cn.nekocode.rxlifecycle.LifecycleEvent
import cn.nekocode.rxlifecycle.compact.RxLifecycleCompact
import com.base.wanandroid.base.BaseFragment
import com.base.wanandroid.databinding.FragmentSquareBinding
import com.base.wanandroid.databinding.FragmentTreeBinding
import com.base.wanandroid.ui.adapter.ArticleAdapter
import com.base.wanandroid.ui.adapter.TreeAdapter
import com.base.wanandroid.ui.home.ArticleDiffCallBack
import com.base.wanandroid.utils.RxTransformer
import com.base.wanandroid.utils.lifecycleOwner
import com.drake.brv.PageRefreshLayout

/**
 * @author jiangshiyu
 * @date 2022/6/2
 */
class TreeFragment : BaseFragment<FragmentTreeBinding, TreeViewModel>() {

    //是否初次切换
    private var first = true

    private val treeAdapter by lazy { TreeAdapter() }

    override fun onBundle(bundle: Bundle) {
    }

    override fun init(savedInstanceState: Bundle?) {

        if (first) {
            binding?.rv?.adapter = treeAdapter

            binding?.page?.onRefresh {

                //获取体系数据
                viewModel.getTreeList()
                    .compose(
                        RxLifecycleCompact.bind(this@TreeFragment)
                            .disposeObservableWhen(LifecycleEvent.DESTROY_VIEW)
                    )
                    .compose(RxTransformer.async())
                    .subscribe {
                        if (first && it.data.isEmpty()) {
                            showEmpty()
                        } else {
                            first = false
                            treeAdapter.setList(it.data.toMutableList())
                            showContent(false)
                        }
                    }.lifecycleOwner(this@TreeFragment)
            }?.autoRefresh()
        }

    }
}