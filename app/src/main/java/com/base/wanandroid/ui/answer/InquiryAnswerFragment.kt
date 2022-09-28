package com.base.wanandroid.ui.answer

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.wanandroid.base.BaseFragment1
import com.base.wanandroid.databinding.FragmentProjectChildBinding
import com.base.wanandroid.ext.init
import com.base.wanandroid.ext.initFooter
import com.base.wanandroid.ext.loadListData
import com.base.wanandroid.ext.loadServiceInit
import com.base.wanandroid.ext.showLoading
import com.base.wanandroid.ui.adapter.ArticleNewAdapter
import com.base.wanandroid.ui.home.ArticleDiffNewCallBack
import com.base.wanandroid.ui.square.SquareViewModel
import com.base.wanandroid.utils.initFloatBtn
import com.base.wanandroid.widget.recyclerview.DefineLoadMoreView
import com.base.wanandroid.widget.recyclerview.SpaceItemDecoration
import com.blankj.utilcode.util.ConvertUtils
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir

/**
 * @author jiangshiyu
 * @date 2022/6/6
 * 每日一问
 */
class InquiryAnswerFragment : BaseFragment1<SquareViewModel, FragmentProjectChildBinding>() {


    private val articleAdapter by lazy {
        ArticleNewAdapter(this, true).apply {
            this.setDiffCallback(ArticleDiffNewCallBack())
        }
    }

    private lateinit var loadSir: LoadService<Any>

    private lateinit var footView: DefineLoadMoreView


    override fun initView(savedInstanceState: Bundle?) {
        loadSir = loadServiceInit(mViewBind.swipeRefresh) {
            loadSir.showLoading()
            mViewModel.getAskData(true)
        }

        mViewBind.rvList.init(LinearLayoutManager(context), articleAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
            footView = it.initFooter {
                mViewModel.getAskData(false)
            }
            //初始化FloatingActionButton
            it.initFloatBtn(mViewBind.fab)
        }

        mViewBind.swipeRefresh.init {
            mViewModel.getAskData(true)
        }
    }

    override fun lazyLoadData() {
        loadSir.showLoading()
        mViewModel.getAskData(true)
    }

    override fun createObserver() {
        mViewModel.askDataState.observe(viewLifecycleOwner) {
            loadListData(it, articleAdapter, loadSir, mViewBind.rvList, mViewBind.swipeRefresh)
        }
    }


}