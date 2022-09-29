package com.base.wanandroid.ui.project

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.wanandroid.base.BaseFragment
import com.base.wanandroid.databinding.FragmentProjectChildBinding
import com.base.wanandroid.ext.init
import com.base.wanandroid.ext.initFooter
import com.base.wanandroid.ext.loadListData
import com.base.wanandroid.ext.loadServiceInit
import com.base.wanandroid.ext.showLoading
import com.base.wanandroid.ui.adapter.ArticleNewAdapter
import com.base.wanandroid.ui.home.ArticleDiffNewCallBack
import com.base.wanandroid.utils.initFloatBtn
import com.base.wanandroid.widget.recyclerview.DefineLoadMoreView
import com.base.wanandroid.widget.recyclerview.SpaceItemDecoration
import com.blankj.utilcode.util.ConvertUtils
import com.kingja.loadsir.core.LoadService

/**
 * @author jiangshiyu
 * @date 2022/5/30
 * 项目体系下子fragment
 */
class ProjectChildFragment : BaseFragment<ProjectViewModel, FragmentProjectChildBinding>() {

    companion object {
        /**
         * 实例化fragment
         * @param cid 项目id
         * @param isNew 是否最新
         * @return fragment对象
         */
        fun newInstance(cid: Int, isNew: Boolean = false): ProjectChildFragment {
            val args = Bundle().apply {
                putInt("cid", cid)
                putBoolean("isNew", isNew)
            }
            val fragment = ProjectChildFragment()
            fragment.arguments = args
            return fragment
        }
    }


    private val articleAdapter by lazy {
        ArticleNewAdapter(this, true).apply {
            this.setDiffCallback(ArticleDiffNewCallBack())
        }
    }

    private lateinit var loadSir: LoadService<Any>

    private lateinit var footView: DefineLoadMoreView

    //项目对应的cid
    private var cid = 0

    //是否是最新项目
    private var isNew = false


    override fun initView(savedInstanceState: Bundle?) {
        arguments?.let {
            cid = it.getInt("cid")
            isNew = it.getBoolean("isNew")
        }
        //状态页配置
        loadSir = loadServiceInit(mViewBind.swipeRefresh) {
            loadSir.showLoading()
            mViewModel.getProjectData(true, cid, isNew)
        }

        mViewBind.rvList.init(LinearLayoutManager(context), articleAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
            footView = it.initFooter {
                //加载更多请求数据
                mViewModel.getProjectData(false, cid, isNew)
            }
            it.initFloatBtn(mViewBind.fab)
        }

        //初始化swipeRefreshLayout
        mViewBind.swipeRefresh.init {
            mViewModel.getProjectData(true, cid, isNew)
        }
    }

    override fun lazyLoadData() {
        loadSir.showLoading()
        mViewModel.getProjectData(true, cid, isNew)
    }

    override fun createObserver() {
        mViewModel.projectDataState.observe(viewLifecycleOwner) {
            loadListData(it, articleAdapter, loadSir, mViewBind.rvList, mViewBind.swipeRefresh)
        }
    }

}