package com.base.wanandroid.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.nekocode.rxlifecycle.LifecycleEvent
import cn.nekocode.rxlifecycle.compact.RxLifecycleCompact
import com.base.wanandroid.R
import com.base.wanandroid.base.BaseFragment
import com.base.wanandroid.databinding.FragmentHomeBinding
import com.base.wanandroid.ui.adapter.ArticleAdapter
import com.base.wanandroid.ui.adapter.ImageTitleAdapter
import com.base.wanandroid.ui.collect.ArticleViewModel
import com.base.wanandroid.ui.collect.CollectActivity
import com.base.wanandroid.ui.history.HistoryRecordActivity
import com.base.wanandroid.ui.integral.IntegralActivity
import com.base.wanandroid.ui.integral.LeaderBoardActivity
import com.base.wanandroid.ui.search.SearchActivity
import com.base.wanandroid.ui.setting.SettingActivity
import com.base.wanandroid.ui.share.ShareActivity
import com.base.wanandroid.ui.user.LoginActivity
import com.base.wanandroid.utils.AppConfig
import com.base.wanandroid.utils.RxTransformer
import com.base.wanandroid.utils.initFloatBtn
import com.base.wanandroid.utils.lifecycleOwner
import com.base.wanandroid.widget.Dialog
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.drake.brv.PageRefreshLayout
import com.drake.serialize.intent.openActivity
import com.google.android.material.imageview.ShapeableImageView
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.transformer.ZoomOutPageTransformer
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

/**
 * @author jiangshiyu
 * @date 2022/3/7
 * 主页
 */
class HomeFragment : BaseFragment<FragmentHomeBinding, ArticleViewModel>() {

    private val articleAdapter by lazy {
        ArticleAdapter(this, viewModel, true).apply {
            this.setDiffCallback(ArticleDiffCallBack())
        }
    }


    private lateinit var startLoginLaunch: ActivityResultLauncher<Intent>

    override fun onBundle(bundle: Bundle) {

    }

    override fun init(savedInstanceState: Bundle?) {
        initData()
        startLoginLaunch =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    if (AppConfig.UserName.isNotEmpty()) {
                        //重建
                        initData()
                    }
                }
            }
    }


    private fun initData() {
        //初始化视图
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
        setHasOptionsMenu(true)
        binding?.toolbar?.title = getString(R.string.home_name)
        binding?.toolbar?.inflateMenu(R.menu.menu_toolbar_search)
        binding?.toolbar?.setOnMenuItemClickListener {
            if (it.itemId == R.id.search) {
                openActivity<SearchActivity>()
            }
            true
        }

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

            //点击头像
            it.getHeaderView(0).run {
                //积分排名图标
                val rankImage = findViewById<ImageView>(R.id.rank_image)
                //用户头像
                val headerImage = findViewById<ShapeableImageView>(R.id.header_image)
                //用户名
                findViewById<TextView>(R.id.user_text).text =
                    AppConfig.UserName.ifEmpty { getString(R.string.my_user) }
                //等级文字
                findViewById<TextView>(R.id.level_text).text =
                    AppConfig.Level.ifEmpty { getString(R.string.my_ellipsis) }
                //排名文字
                findViewById<TextView>(R.id.rank_text).text =
                    AppConfig.Rank.ifEmpty { getString(R.string.my_ellipsis) }
                //积分项设置文本
                val navMenu = it.menu.findItem(R.id.nav_integral).actionView as TextView

                navMenu.gravity = Gravity.CENTER_VERTICAL
                navMenu.text = AppConfig.CoinCount.ifEmpty { "" }

                //如果没有登录
                if (AppConfig.UserName.isEmpty()) {
                    headerImage.setOnClickListener {
                        startLoginLaunch.launch(LoginActivity.start(context))
                    }
                }

                rankImage.setOnClickListener {
                    openActivity<LeaderBoardActivity>()
                }
            }

            //未登录隐藏登出项，登陆可见
            it.menu.findItem(R.id.nav_exit).isVisible = AppConfig.UserName.isNotEmpty()


            it.setNavigationItemSelectedListener { menu ->
                when (menu.itemId) {
                    R.id.nav_integral -> {
                        if (AppConfig.UserName.isEmpty()) {
                            ToastUtils.showShort(getString(R.string.please_login))
                            startLoginLaunch.launch(LoginActivity.start(requireContext()))
                        } else {
                            //积分页
                            openActivity<IntegralActivity>()
                        }
                    }
                    R.id.nav_collect -> {
                        if (AppConfig.UserName.isEmpty()) {
                            ToastUtils.showShort(getString(R.string.please_login))
                            startLoginLaunch.launch(LoginActivity.start(requireContext()))
                        } else {
                            //我的收藏页
                            openActivity<CollectActivity>()
                        }

                    }

                    R.id.nav_share -> {
                        if (AppConfig.UserName.isEmpty()) {
                            ToastUtils.showShort(getString(R.string.please_login))
                            startLoginLaunch.launch(LoginActivity.start(requireContext()))
                        } else {
                            //分享文章，内容
                            openActivity<ShareActivity>()
                        }
                    }
                    R.id.nav_record -> {
                        //历史记录
                        openActivity<HistoryRecordActivity>()
                    }
                    R.id.nav_setting -> {
                        openActivity<SettingActivity>()
                    }
                    R.id.nav_exit -> {
                        //离开
                        Dialog.getConfirmDialog(
                            requireContext(),
                            getString(R.string.exit_confirm)
                        ) { _, _ ->
                            viewModel.loginOut()
                                .compose(RxTransformer.async())
                                .subscribe {
                                    //从存储中清除cookie、个人信息
                                    AppConfig.Cookie.clear()
                                    AppConfig.UserName = ""
                                    AppConfig.PassWord = ""
                                    AppConfig.Level = ""
                                    AppConfig.Rank = ""
                                    AppConfig.CoinCount = ""

                                    initData()
                                    ToastUtils.showShort(getString(R.string.exited_success))
                                }.lifecycleOwner(requireActivity())
                        }.show()
                    }
                }
                true
            }
        }
    }


    private fun initView() {
        binding?.rvHomeList?.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = articleAdapter
            binding?.fab?.let { initFloatBtn(it) }
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
                        articleAdapter.setList(it.data.data.datas.toMutableList())
                    }, {
                        Log.e(TAG, "onRefresh: ${it.message}")
                        showError()
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
                view
                viewModel.getArticleList(index)
                    .timeout(30, TimeUnit.SECONDS)
                    .compose(
                        RxLifecycleCompact.bind(this@HomeFragment)
                            .disposeObservableWhen(LifecycleEvent.DESTROY_VIEW)
                    )
                    .compose(RxTransformer.async())
                    .subscribe({
                        if (it.data.data.datas.isNullOrEmpty()) {
                            //没有更多内容
                            showContent(false)
                            return@subscribe
                        }
                        articleAdapter.addData(it.data.data.datas)
                        //翻页
                        index += 1
                        showContent(true)
                    }, {
                        Log.e(TAG, "onLoadMore:${it.message} ")
                        showError()
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
                Log.e(TAG, "getBannerData: ${it.message}")
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