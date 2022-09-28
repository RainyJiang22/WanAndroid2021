package com.base.wanandroid.ui.tree

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.nekocode.rxlifecycle.LifecycleEvent
import cn.nekocode.rxlifecycle.compact.RxLifecycleCompact
import com.base.wanandroid.base.BaseFragment
import com.base.wanandroid.base.BaseFragment1
import com.base.wanandroid.databinding.FragmentChildBinding
import com.base.wanandroid.databinding.FragmentProjectChildBinding
import com.base.wanandroid.ext.init
import com.base.wanandroid.ext.initFooter
import com.base.wanandroid.ext.loadListData
import com.base.wanandroid.ext.loadServiceInit
import com.base.wanandroid.ext.showLoading
import com.base.wanandroid.ui.adapter.ArticleAdapter
import com.base.wanandroid.ui.adapter.ArticleNewAdapter
import com.base.wanandroid.ui.collect.ArticleViewModel
import com.base.wanandroid.ui.home.ArticleDiffCallBack
import com.base.wanandroid.ui.home.ArticleDiffNewCallBack
import com.base.wanandroid.utils.RxTransformer
import com.base.wanandroid.utils.initFloatBtn
import com.base.wanandroid.utils.lifecycleOwner
import com.base.wanandroid.widget.recyclerview.DefineLoadMoreView
import com.base.wanandroid.widget.recyclerview.SpaceItemDecoration
import com.blankj.utilcode.util.ConvertUtils
import com.drake.brv.PageRefreshLayout
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir

/**
 * @author jiangshiyu
 * @date 2022/6/2
 */
class TreeChildFragment : BaseFragment1<TreeViewModel, FragmentProjectChildBinding>() {


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

    private lateinit var loadSir: LoadService<Any>

    private lateinit var footView: DefineLoadMoreView


    private val articleAdapter by lazy {
        ArticleNewAdapter(this, true).apply {
            this.setDiffCallback(ArticleDiffNewCallBack())
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        arguments?.let {
            cid = it.getInt("cid")
        }

        loadSir = loadServiceInit(mViewBind.swipeRefresh) {
            loadSir.showLoading()
            mViewModel.getSystemChildData(true, cid)
        }

        mViewBind.rvList.init(LinearLayoutManager(context), articleAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
            footView = it.initFooter {
                mViewModel.getSystemChildData(false, cid)
            }
            //初始化FloatingActionButton
            it.initFloatBtn(mViewBind.fab)
        }

        mViewBind.swipeRefresh.init {
            mViewModel.getSystemChildData(true, cid)
        }

    }

    override fun lazyLoadData() {
        //设置界面 加载中
        loadSir.showLoading()
        mViewModel.getSystemChildData(true, cid)
    }

    override fun createObserver() {
       mViewModel.systemChildDataState.observe(viewLifecycleOwner) {
           loadListData(it, articleAdapter, loadSir, mViewBind.rvList, mViewBind.swipeRefresh)
       }
    }
}