package com.base.wanandroid.ui.project

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.base.wanandroid.base.BaseFragment
import com.base.wanandroid.databinding.FragmentProjectBinding
import com.base.wanandroid.ext.loadServiceInit
import com.base.wanandroid.ext.setErrorText
import com.base.wanandroid.ext.showLoading
import com.base.wanandroid.utils.bindViewPager2
import com.base.wanandroid.utils.init
import com.base.wanandroid.widget.loadcallback.ErrorCallBack
import com.kingja.loadsir.core.LoadService
import me.hgj.jetpackmvvm.ext.parseState

/**
 * @author jiangshiyu
 * @date 2022/3/7
 */
class ProjectFragment : BaseFragment<ProjectViewModel, FragmentProjectBinding>() {

    companion object {
        const val TAG = "ProjectFragment"

    }

    //界面状态管理
    private lateinit var loadSir: LoadService<Any>

    /** 标题分类集合 */
    private val classifyList: ArrayList<String> by lazy { arrayListOf() }

    //子项目体系fragment集合
    private val fragments: ArrayList<Fragment> by lazy { arrayListOf() }


    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        loadSir = loadServiceInit(mViewBind.contentLayout.viewPager) {
            //重试操作
            loadSir.showLoading()
            mViewModel.getProjectTitleData()
        }

        mViewBind.contentLayout.let {
            it.viewPager.init(this, fragments)
            it.magicIndicator.bindViewPager2(it.viewPager, classifyList)
        }

    }


    override fun lazyLoadData() {
        super.lazyLoadData()
        loadSir.showLoading()
        mViewModel.getProjectTitleData()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun createObserver() {
        super.createObserver()
        mViewModel.titleData.observe(viewLifecycleOwner) { data ->
            parseState(data, {
                fragments.clear()
                classifyList.clear()
                classifyList.add("最新项目")
                classifyList.addAll(it.map { it.name })
                fragments.add(ProjectChildFragment.newInstance(0, true))
                it.forEach { classify ->
                    fragments.add(ProjectChildFragment.newInstance(classify.id, false))
                }
                mViewBind.contentLayout.apply {
                    magicIndicator.navigator.notifyDataSetChanged()
                    viewPager.adapter?.notifyDataSetChanged()
                    viewPager.offscreenPageLimit = fragments.size
                    loadSir.showSuccess()
                }

            }, {
                Log.e(TAG, "the error is ${it.errorMsg}")
                //失败
                loadSir.showCallback(ErrorCallBack::class.java)
                loadSir.setErrorText(it.errorMsg)
            })
        }
    }

}