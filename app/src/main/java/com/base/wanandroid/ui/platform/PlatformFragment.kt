package com.base.wanandroid.ui.platform

import android.os.Bundle
import androidx.fragment.app.Fragment
import cn.nekocode.rxlifecycle.LifecycleEvent
import cn.nekocode.rxlifecycle.compact.RxLifecycleCompact
import com.base.wanandroid.R
import com.base.wanandroid.base.BaseFragment
import com.base.wanandroid.databinding.FragmentPlatformBinding
import com.base.wanandroid.ui.project.ProjectChildFragment
import com.base.wanandroid.utils.RxTransformer
import com.base.wanandroid.utils.bindViewPager2
import com.base.wanandroid.utils.init
import com.base.wanandroid.utils.lifecycleOwner

/**
 * @author jiangshiyu
 * @date 2022/5/31
 * 公众号fragment
 */
class PlatformFragment : BaseFragment<FragmentPlatformBinding, PlatformViewModel>() {


    /** fragment集合 */
    private val fragments: ArrayList<Fragment> by lazy { arrayListOf() }

    /** 分类集合 */
    private val classifyList: ArrayList<String> by lazy { arrayListOf() }


    override fun onBundle(bundle: Bundle) {

    }

    override fun init(savedInstanceState: Bundle?) {
        binding?.toolbar?.title = getString(R.string.platform_fragment)
        loadPlatformList()
    }


    /**
     * 加载项目体系
     */
    private fun loadPlatformList() {
        viewModel.getPlatFormList()
            .compose(
                RxLifecycleCompact.bind(this).disposeObservableWhen(
                    LifecycleEvent.DESTROY_VIEW
                )
            )
            .compose(RxTransformer.async())
            .subscribe { classify ->
                //所有分类
                classifyList.addAll(classify.data.map { it.name })

                classify.data.forEach {
                    fragments.add(PlatformChildFragment.newInstance(it.id))
                }
                //初始化
                binding?.contentLayout?.let {
                    it.viewPager.init(this, fragments)
                    it.magicIndicator.bindViewPager2(it.viewPager, classifyList)
                    it.viewPager.offscreenPageLimit = fragments.size
                }
            }.lifecycleOwner(this)

    }
}