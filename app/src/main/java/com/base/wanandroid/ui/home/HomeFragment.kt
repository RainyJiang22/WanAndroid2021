package com.base.wanandroid.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.nekocode.rxlifecycle.LifecycleEvent
import cn.nekocode.rxlifecycle.compact.RxLifecycleCompact
import com.base.wanandroid.BuildConfig
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