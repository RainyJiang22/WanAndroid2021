package com.base.wanandroid.ui.project

import android.os.Bundle
import androidx.fragment.app.Fragment
import cn.nekocode.rxlifecycle.LifecycleEvent
import cn.nekocode.rxlifecycle.compact.RxLifecycleCompact
import com.base.wanandroid.R
import com.base.wanandroid.base.BaseFragment
import com.base.wanandroid.databinding.FragmentProjectBinding
import com.base.wanandroid.utils.RxTransformer
import com.base.wanandroid.utils.bindViewPager2
import com.base.wanandroid.utils.init
import com.base.wanandroid.utils.lifecycleOwner

/**
 * @author jiangshiyu
 * @date 2022/3/7
 */
class ProjectFragment : BaseFragment<FragmentProjectBinding, ProjectViewModel>() {

    companion object {
        const val TAG = "TreeFragment"

    }

    /** 分类集合 */
    private val classifyList: ArrayList<String> by lazy { arrayListOf() }

    //子项目体系fragment集合
    private val fragments: ArrayList<Fragment> by lazy { arrayListOf() }


    override fun onBundle(bundle: Bundle) {

    }

    override fun init(savedInstanceState: Bundle?) {
        loadProjectType()
    }

    /**
     * 加载项目体系
     */
    private fun loadProjectType() {
        viewModel.getProjectType()
            .compose(
                RxLifecycleCompact.bind(this).disposeObservableWhen(
                    LifecycleEvent.DESTROY_VIEW
                )
            )
            .compose(RxTransformer.async())
            .subscribe { classify ->
                //最新项目tab
                classifyList.add(getString(R.string.new_project))
                //所有分类tab加上
                classifyList.addAll(classify.data.map { it.name })

                //先创建最新项目的fragment
                fragments.add(ProjectChildFragment.newInstance(0, true))

                classify.data.forEach {
                    fragments.add(ProjectChildFragment.newInstance(it.id, false))
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