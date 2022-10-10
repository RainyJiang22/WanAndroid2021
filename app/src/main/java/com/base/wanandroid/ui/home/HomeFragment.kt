package com.base.wanandroid.ui.home

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.wanandroid.R
import com.base.wanandroid.application.appViewModel
import com.base.wanandroid.application.eventViewModel
import com.base.wanandroid.base.BaseFragment
import com.base.wanandroid.databinding.FragmentHomeBinding
import com.base.wanandroid.ext.init
import com.base.wanandroid.ext.initFooter
import com.base.wanandroid.ext.loadListData
import com.base.wanandroid.ext.loadServiceInit
import com.base.wanandroid.ext.showLoading
import com.base.wanandroid.ui.adapter.ArticleNewAdapter
import com.base.wanandroid.ui.adapter.ImageTitleAdapter
import com.base.wanandroid.ui.collect.CollectActivity
import com.base.wanandroid.ui.collect.CollectBus
import com.base.wanandroid.ui.history.HistoryRecordActivity
import com.base.wanandroid.ui.integral.IntegralActivity
import com.base.wanandroid.ui.integral.LeaderBoardActivity
import com.base.wanandroid.ui.search.SearchActivity
import com.base.wanandroid.ui.setting.SettingActivity
import com.base.wanandroid.utils.AppConfig
import com.base.wanandroid.utils.RxTransformer
import com.base.wanandroid.utils.initFloatBtn
import com.base.wanandroid.utils.lifecycleOwner
import com.base.wanandroid.viewmodel.request.RequestCollectViewModel
import com.base.wanandroid.viewmodel.request.RequestHomeViewModel
import com.base.wanandroid.viewmodel.state.HomeViewModel
import com.base.wanandroid.widget.Dialog
import com.base.wanandroid.widget.recyclerview.DefineLoadMoreView
import com.base.wanandroid.widget.recyclerview.SpaceItemDecoration
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.drake.serialize.intent.openActivity
import com.google.android.material.imageview.ShapeableImageView
import com.kingja.loadsir.core.LoadService
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.transformer.ZoomOutPageTransformer
import me.hgj.jetpackmvvm.ext.nav
import me.hgj.jetpackmvvm.ext.navigateAction
import me.hgj.jetpackmvvm.ext.parseState

/**
 * @author jiangshiyu
 * @date 2022/3/7
 * 主页
 */
class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    private val articleAdapter by lazy {
        ArticleNewAdapter(this, true).apply {
            this.setDiffCallback(ArticleDiffNewCallBack())
        }
    }


    //请求数据ViewModel
    private val requestHomeViewModel: RequestHomeViewModel by viewModels()

    //收藏viewModel
    private val requestCollectViewModel: RequestCollectViewModel by viewModels()

    //加载更多view
    private lateinit var footView: DefineLoadMoreView

    //界面状态管理者
    private lateinit var loadSir: LoadService<Any>


    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        //状态页配置
        loadSir = loadServiceInit(mViewBind.homeLayout) {
            //重试时触发操作
            loadSir.showLoading()
            requestHomeViewModel.getHomeData(true)
            requestHomeViewModel.getBannerData()
        }


        mViewBind.rvHomeList.init(LinearLayoutManager(context), articleAdapter).let {
            it.addItemDecoration(
                SpaceItemDecoration(0, ConvertUtils.dp2px(8f), false)
            )
            footView = it.initFooter {
                requestHomeViewModel.getHomeData(false)
            }
            it.initFloatBtn(mViewBind.fab)
        }

        //初始化swipeRefreshLayout
        mViewBind.swipeRefresh.init {
            requestHomeViewModel.getHomeData(true)
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

    override fun initData() {
        super.initData()
    }


    override fun createObserver() {

        //监听用户是否登录，如果未登录
        appViewModel.run {
            userInfo.observeInFragment(this@HomeFragment) {
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


        requestCollectViewModel.collectUiState.observe(viewLifecycleOwner) {
            if (it.isSuccess) {
                eventViewModel.collectEvent.value = CollectBus(it.id, it.collect)
            } else {
                //收藏失败
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


        //监听全局收藏信息, 收藏的Id跟本列表的数据id匹配则需要更新
        eventViewModel.collectEvent.observeInFragment(this) {
            for (index in articleAdapter.data.indices) {
                if (articleAdapter.data[index].id == it.id) {
                    articleAdapter.data[index].collect = it.collect
                    articleAdapter.notifyItemChanged(index)
                    break
                }
            }
        }


        requestHomeViewModel.run {
            //监听文章请求列表数据变化
            homeDataState.observe(viewLifecycleOwner) {
                loadListData(
                    it,
                    articleAdapter,
                    loadSir,
                    mViewBind.rvHomeList,
                    mViewBind.swipeRefresh
                )
            }
            //监听轮播图请求数据变化
            bannerData.observe(viewLifecycleOwner, Observer { resultState ->
                parseState(resultState, { data ->
                    if (data.isNotEmpty()) {
                        mViewBind.banner.apply {
                            setAdapter(
                                ImageTitleAdapter(
                                    context,
                                    Glide.with(this@HomeFragment),
                                    data
                                )
                            )
                            setPageTransformer(ZoomOutPageTransformer())
                            indicator = CircleIndicator(requireContext())
                            addBannerLifecycleObserver(viewLifecycleOwner)
                        }
                    }
                }, {
                    Log.e(TAG, "getBannerData: ${it.message}")
                    it.printStackTrace()
                })
            })
        }

    }

    override fun lazyLoadData() {
        //加载中
        loadSir.showLoading()
        //请求轮播图数据
        requestHomeViewModel.getBannerData()
        //请求文章列表数据
        requestHomeViewModel.getHomeData(true)
    }

    companion object {
        const val TAG = "HomeFragment"
    }
}