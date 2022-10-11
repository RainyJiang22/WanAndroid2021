package com.base.wanandroid.ui.search

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.wanandroid.application.appViewModel
import com.base.wanandroid.application.eventViewModel
import com.base.wanandroid.base.BaseActivity
import com.base.wanandroid.databinding.ActivitySearchResultBinding
import com.base.wanandroid.ext.init
import com.base.wanandroid.ext.initFooter
import com.base.wanandroid.ext.loadServiceInit
import com.base.wanandroid.ext.setErrorText
import com.base.wanandroid.ext.showEmpty
import com.base.wanandroid.ext.showLoading
import com.base.wanandroid.ui.adapter.ArticleNewAdapter
import com.base.wanandroid.ui.collect.ArticleViewModel
import com.base.wanandroid.ui.collect.CollectBus
import com.base.wanandroid.ui.home.ArticleDiffNewCallBack
import com.base.wanandroid.utils.initFloatBtn
import com.base.wanandroid.viewmodel.request.RequestCollectViewModel
import com.base.wanandroid.viewmodel.request.RequestSearchViewModel
import com.base.wanandroid.widget.loadcallback.ErrorCallBack
import com.base.wanandroid.widget.recyclerview.SpaceItemDecoration
import com.blankj.utilcode.util.ConvertUtils
import com.drake.brv.PageRefreshLayout
import com.drake.serialize.intent.bundle
import com.kingja.loadsir.core.LoadService
import com.rainy.monitor.utils.viewModels
import me.hgj.jetpackmvvm.ext.parseState

/**
 * @author jiangshiyu
 * @date 2022/6/16
 */
class SearchResultActivity : BaseActivity<ArticleViewModel, ActivitySearchResultBinding>() {


    private val articleAdapter by lazy {
        ArticleNewAdapter(this, true).apply {
            this.setDiffCallback(ArticleDiffNewCallBack())
        }
    }

    //收藏ViewModel
    private val requestCollectViewModel: RequestCollectViewModel by viewModels()

    //搜索viewModel
    private val requestSearchViewModel: RequestSearchViewModel by viewModels()

    //界面状态管理
    private lateinit var loadSir: LoadService<Any>

    private val key: String by bundle()

    override fun initView(savedInstanceState: Bundle?) {
        PageRefreshLayout.startIndex = 0
        mViewBind.titleBar.title = key
        //标题栏返回按钮关闭页面
        mViewBind.titleBar.leftView?.setOnClickListener { finish() }


        loadSir = loadServiceInit(mViewBind.swipeRefresh) {
            loadSir.showLoading()
            requestSearchViewModel.getSearchResultData(key, true)
        }
        //初始化recyclerView
        mViewBind.rvList.init(LinearLayoutManager(this), articleAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
            it.initFooter {
                //触发加载更多时请求数据
                requestSearchViewModel.getSearchResultData(key, false)
            }
            //初始化FloatingActionButton
            it.initFloatBtn(mViewBind.fab)
        }

        //初始化 SwipeRefreshLayout
        mViewBind.swipeRefresh.init {
            requestSearchViewModel.getSearchResultData(key, true)
        }

        articleAdapter.run {
            setCollectClick { item, v, position ->
                if (v.isChecked) {
                    requestCollectViewModel.unCollect(item.id)
                } else {
                    requestCollectViewModel.collect(item.id)
                }

            }
        }

    }

    override fun onResume() {
        super.onResume()
        loadSir.showLoading()
        requestSearchViewModel.getSearchResultData(key, true)
    }


    override fun createObserver() {
        requestSearchViewModel.searchResultData.observe(this) { resultState ->
            parseState(resultState, {
                mViewBind.swipeRefresh.isRefreshing = false
                //请求成功
                requestSearchViewModel.pageNo++
                if (it.isRefresh() && it.datas.size == 0) {
                    loadSir.showEmpty()
                } else if (it.isRefresh()) {
                    loadSir.showSuccess()
                    articleAdapter.setList(it.datas)
                } else {
                    //不是第一页
                    loadSir.showSuccess()
                    articleAdapter.addData(it.datas)
                }

                mViewBind.rvList.loadMoreFinish(it.isEmpty(), it.hasMore())
            }, {
                //请求失败
                mViewBind.swipeRefresh.isRefreshing = false
                if (articleAdapter.data.size == 0) {
                    loadSir.setErrorText(it.errorMsg)
                    loadSir.showCallback(ErrorCallBack::class.java)
                } else {
                    mViewBind.rvList.loadMoreError(0,it.errorMsg)
                }
            })
        }

        requestCollectViewModel.collectUiState.observe(this) {
            if (it.isSuccess) {
                eventViewModel.collectEvent.value = CollectBus(it.id, it.collect)
            } else {
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
            //监听账户信息是否改变 有值时(登录)将相关的数据设置为已收藏，为空时(退出登录)，将已收藏的数据变为未收藏
            userInfo.observeInActivity(this@SearchResultActivity) {
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

            //监听全局的收藏信息 收藏的Id跟本列表的数据id匹配则需要更新
            eventViewModel.collectEvent.observeInActivity(this@SearchResultActivity) {
                for (index in articleAdapter.data.indices) {
                    if (articleAdapter.data[index].id == it.id) {
                        articleAdapter.data[index].collect = it.collect
                        articleAdapter.notifyItemChanged(index)
                        break
                    }
                }
            }

        }
    }

}