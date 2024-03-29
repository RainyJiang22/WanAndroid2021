package com.base.wanandroid.ui.answer

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.wanandroid.application.appViewModel
import com.base.wanandroid.application.eventViewModel
import com.base.wanandroid.base.BaseFragment
import com.base.wanandroid.databinding.FragmentProjectChildBinding
import com.base.wanandroid.ext.init
import com.base.wanandroid.ext.initFooter
import com.base.wanandroid.ext.loadListData
import com.base.wanandroid.ext.loadServiceInit
import com.base.wanandroid.ext.showLoading
import com.base.wanandroid.ui.adapter.ArticleNewAdapter
import com.base.wanandroid.ui.collect.CollectBus
import com.base.wanandroid.ui.home.ArticleDiffNewCallBack
import com.base.wanandroid.ui.square.SquareViewModel
import com.base.wanandroid.utils.initFloatBtn
import com.base.wanandroid.viewmodel.request.RequestCollectViewModel
import com.base.wanandroid.widget.recyclerview.DefineLoadMoreView
import com.base.wanandroid.widget.recyclerview.SpaceItemDecoration
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ToastUtils
import com.kingja.loadsir.core.LoadService

/**
 * @author jiangshiyu
 * @date 2022/6/6
 * 每日一问
 */
class InquiryAnswerFragment : BaseFragment<SquareViewModel, FragmentProjectChildBinding>() {


    private val articleAdapter by lazy {
        ArticleNewAdapter(this, true).apply {
            this.setDiffCallback(ArticleDiffNewCallBack())
        }
    }

    private lateinit var loadSir: LoadService<Any>

    private lateinit var footView: DefineLoadMoreView

    //收藏ViewModel
    private val requestCollectViewModel: RequestCollectViewModel by viewModels()

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

        articleAdapter.run {
            setCollectClick { item, v, position ->
                if (v.isChecked) {
                    requestCollectViewModel.collect(item.id)
                } else {
                    requestCollectViewModel.unCollect(item.id)
                }
            }
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



        requestCollectViewModel.collectUiState.observe(viewLifecycleOwner) {
            if (it.isSuccess) {
                eventViewModel.collectEvent.value = CollectBus(it.id, it.collect)
            } else {
                ToastUtils.showShort(it.errorMsg)
                for (index in articleAdapter.data.indices) {
                    if (articleAdapter.data[index].id == it.id) {
                        articleAdapter.data[index].collect = it.collect
                        articleAdapter.notifyItemChanged(index)
                        break
                    }
                }
            }
        }

        appViewModel.run {
            //监听用户是否退出登录
            userInfo.observeInFragment(this@InquiryAnswerFragment) {
                if (it != null) {
                    it.collectIds.forEach { id ->
                        for (item in articleAdapter.data) {
                            if (id.toInt() == item.id) {
                                item.collect = true
                                break
                            }
                        }
                    }
                } else {
                    for (item in articleAdapter.data) {
                        item.collect = false
                    }
                }
                articleAdapter.notifyDataSetChanged()
            }
        }
    }


}