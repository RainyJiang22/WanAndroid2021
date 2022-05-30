package com.base.wanandroid.ui.tree

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.nekocode.rxlifecycle.LifecycleEvent
import cn.nekocode.rxlifecycle.compact.RxLifecycleCompact
import com.base.wanandroid.base.BaseFragment
import com.base.wanandroid.bean.ArticleListResponse
import com.base.wanandroid.databinding.FragmentTreeChildBinding
import com.base.wanandroid.ui.adapter.ArticleAdapter
import com.base.wanandroid.ui.home.ArticleDiffCallBack
import com.base.wanandroid.utils.RxTransformer
import com.base.wanandroid.utils.lifecycleOwner

/**
 * @author jiangshiyu
 * @date 2022/5/30
 * 项目体系下子fragment
 */
class TreeChildFragment : BaseFragment<FragmentTreeChildBinding, TreeViewModel>() {

    companion object {
        /**
         * 实例化fragment
         * @param cid 项目id
         * @param isNew 是否最新
         * @return fragment对象
         */
        fun newInstance(cid: Int, isNew: Boolean = false): TreeChildFragment {
            val args = Bundle().apply {
                putInt("cid", cid)
                putBoolean("isNew", isNew)
            }
            val fragment = TreeChildFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var cid = 0
    private var isNew = false

    private val articleAdapter by lazy {
        ArticleAdapter(true).apply {
            this.setDiffCallback(ArticleDiffCallBack())
        }
    }

    override fun onBundle(bundle: Bundle) {

    }

    override fun init(savedInstanceState: Bundle?) {
        arguments?.let {
            cid = it.getInt("cid")
            isNew = it.getBoolean("isNew")
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
            if (isNew) {
                loadNewProject(index, {
                    if (it.data.datas.isEmpty()) {
                        showEmpty()
                    }
                    index += if (isNew && index == 0 || !isNew && index == 1) {
                        articleAdapter.setList(it.data.datas)
                        1
                    } else {
                        if (it.data.datas.isNullOrEmpty()) {
                            showContent(false)
                            return@loadNewProject
                        }
                        articleAdapter.addData(it.data.datas)
                        1
                    }
                }, {
                    showError()
                })
            } else {
                loadProjectList(index, cid, {
                    if (it.data.datas.isEmpty()) {
                        showEmpty()
                    }
                    index += if (isNew && index == 0 || !isNew && index == 1) {
                        articleAdapter.setList(it.data.datas)
                        1
                    } else {
                        if (it.data.datas.isNullOrEmpty()) {
                            showContent(false)
                            return@loadProjectList
                        }
                        articleAdapter.addData(it.data.datas)
                        1
                    }
                }, {
                    showError()
                })
            }
            showContent(true)
        }?.autoRefresh()
    }


    /**
     * 加载最新项目体系数据
     */
    private fun loadNewProject(
        page: Int,
        onNext: (data: ArticleListResponse) -> Unit,
        onError: () -> Unit
    ) {
        viewModel.getNewProject(page)
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

    /**
     * 加载项目体系数据
     */
    private fun loadProjectList(
        page: Int, cid: Int,
        onNext: (data: ArticleListResponse) -> Unit,
        onError: () -> Unit
    ) {
        viewModel.getProjectList(page, cid)
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