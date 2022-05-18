package com.base.wanandroid.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
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
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.transformer.ZoomOutPageTransformer
import java.util.concurrent.TimeUnit

/**
 * @author jiangshiyu
 * @date 2022/3/7
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
        getArticleList()
        getBannerData()
    }


    private fun initView() {

        binding?.rvHomeList?.also {
            it.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            it.adapter = articleAdapter
        }
    }

    /**
     * 主页数据
     */
    private fun getArticleList() {
        viewModel.getArticleList(0)
            .timeout(30, TimeUnit.SECONDS)
            .compose(
                RxLifecycleCompact.bind(this)
                    .disposeObservableWhen(LifecycleEvent.DESTROY_VIEW)
            )
            .compose(RxTransformer.async())
            .subscribe({
                if (BuildConfig.LOG_ENABLE) {
                    Log.d(TAG, "getHomeData: $it")
                }
                //设置文章数据
                articleAdapter.setDiffNewData(it.data.datas.toMutableList())

            }, {
                if (BuildConfig.LOG_ENABLE) {
                    Log.d(TAG, "getHomeDataError: $it")
                }
            }).lifecycleOwner(this)
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
                if (BuildConfig.LOG_ENABLE) {
                    Log.d(TAG, "getBannerData: $it")
                }

                if (it.data?.isNotEmpty() == true) {
                    binding?.banner?.apply {
                        setAdapter(ImageTitleAdapter(Glide.with(this@HomeFragment), it.data!!))
                        setPageTransformer(ZoomOutPageTransformer())
                        indicator = CircleIndicator(requireContext())
                        addBannerLifecycleObserver(this@HomeFragment)
                    }

                }


            }, {
                if (BuildConfig.LOG_ENABLE) {
                    Log.d(TAG, "getBannerError: $it")
                }

            }).lifecycleOwner(this)
    }


    companion object {
        const val TAG = "HomeFragment"
    }
}