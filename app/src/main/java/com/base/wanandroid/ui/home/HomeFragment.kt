package com.base.wanandroid.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.nekocode.rxlifecycle.LifecycleEvent
import cn.nekocode.rxlifecycle.compact.RxLifecycleCompact
import com.base.wanandroid.BuildConfig
import com.base.wanandroid.R
import com.base.wanandroid.base.BaseFragment
import com.base.wanandroid.databinding.FragmentHomeBinding
import com.base.wanandroid.ui.adapter.ArticleAdapter
import com.base.wanandroid.ui.adapter.ImageTitleAdapter
import com.base.wanandroid.utils.RxTransformer
import com.base.wanandroid.utils.lifecycleOwner
import com.bumptech.glide.Glide
import com.drake.brv.PageRefreshLayout
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.transformer.ZoomOutPageTransformer
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

/**
 * @author jiangshiyu
 * @date 2022/3/7
 * 主页
 */
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    private val articleAdapter by lazy {
        ArticleAdapter(true).apply {
            this.setDiffCallback(ArticleDiffCallBack())
        }
    }

    override fun onBundle(bundle: Bundle) {

    }

    override fun init(savedInstanceState: Bundle?) {
        initView()
        getBannerData()
        //刷新
        onRefresh()
        //加载
        onLoadMore()

        //侧滑栏
        initNavigationView()
        //抽屉布局
        initDrawerLayout()
    }


    private fun initDrawerLayout() {
        binding?.toolbar?.title = getString(R.string.home_name)
        binding?.drawerLayout?.run {
            //开关抽屉时导航按钮的旋转动画效果
            val toggle = ActionBarDrawerToggle(
                requireActivity(),
                this,
                binding?.toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            )
            addDrawerListener(toggle)
            toggle.syncState()

            //侧滑栏
            this.addDrawerListener(object : DrawerLayout.DrawerListener {
                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                    val content = binding?.drawerLayout?.getChildAt(0)
                    val scale = 1 - slideOffset
                    content?.translationX = drawerView.measuredWidth * (1 - scale)
                }

                override fun onDrawerOpened(drawerView: View) {
                }

                override fun onDrawerClosed(drawerView: View) {
                }

                override fun onDrawerStateChanged(newState: Int) {
                }

            })
        }


    }

    private fun initNavigationView() {

        binding?.navView?.let {

            it.setNavigationItemSelectedListener { menu ->
                when (menu.itemId) {
                    R.id.nav_integral -> {
                        //TODO 积分页
                    }
                    R.id.nav_collect -> {
                        //TODO 我的收藏页
                    }

                    R.id.nav_share -> {
                        //TODO 分享

                    }
                    R.id.nav_record -> {
                        //TODO 历史记录
                    }
                    R.id.nav_setting -> {
                        //TODO 设置
                    }
                    R.id.nav_exit -> {
                        //TODO 离开
                    }
                }
                true
            }
        }
    }


    private fun initView() {
        binding?.rvHomeList?.also {
            it.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            it.adapter = articleAdapter
        }
    }


    /**
     * 页面刷新操作，刷新回调,下拉刷新
     */
    private fun onRefresh() {
        binding?.pageHome?.onRefresh {
            lifecycleScope.launch {

                //加载主页数据
                viewModel.getArticleList(index)
                    .timeout(30, TimeUnit.SECONDS)
                    .compose(
                        RxLifecycleCompact.bind(this@HomeFragment)
                            .disposeObservableWhen(LifecycleEvent.DESTROY_VIEW)
                    )
                    .compose(RxTransformer.async())
                    .subscribe({
                        //设置文章数据
                        articleAdapter.setList(it.data.datas.toMutableList())
                    }, {
                        showEmpty()
                    }).lifecycleOwner(this@HomeFragment)
                index += 1
                showContent(true)
            }
            //自动刷新
        }?.autoRefresh()
    }


    /**
     * 加载更多
     */
    private fun onLoadMore() {
        binding?.pageHome?.onLoadMore {
            lifecycleScope.launch {
                viewModel.getArticleList(index)
                    .timeout(30, TimeUnit.SECONDS)
                    .compose(
                        RxLifecycleCompact.bind(this@HomeFragment)
                            .disposeObservableWhen(LifecycleEvent.DESTROY_VIEW)
                    )
                    .compose(RxTransformer.async())
                    .subscribe({
                        if (it.data.datas.isNullOrEmpty()) {
                            //没有更多内容
                            showContent(false)
                            return@subscribe
                        }
                        articleAdapter.addData(it.data.datas)
                        //翻页
                        index += 1
                        showContent(true)
                    }, {
                        showEmpty()
                    }).lifecycleOwner(this@HomeFragment)

            }

        }?.autoRefresh()


    }

    /**
     * Banner数据
     */
    private fun getBannerData() {
        viewModel.getBannerData()
            .compose(
                RxLifecycleCompact.bind(this)
                    .disposeObservableWhen(LifecycleEvent.DESTROY_VIEW)
            )
            .compose(RxTransformer.async())
            .subscribe({
                if (it.data?.isNotEmpty() == true) {
                    binding?.banner?.apply {
                        setAdapter(
                            ImageTitleAdapter(
                                context,
                                Glide.with(this@HomeFragment),
                                it.data!!
                            )
                        )
                        setPageTransformer(ZoomOutPageTransformer())
                        indicator = CircleIndicator(requireContext())
                        addBannerLifecycleObserver(this@HomeFragment)
                    }

                }

            }, {
                it.printStackTrace()
            }).lifecycleOwner(this)
    }

    override fun onResume() {
        super.onResume()
        //设置页面分页初始索引
        PageRefreshLayout.startIndex = 0
    }


    companion object {
        const val TAG = "HomeFragment"
    }
}