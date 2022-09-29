package com.base.wanandroid.ui.platform

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.base.wanandroid.base.BaseFragment
import com.base.wanandroid.databinding.FragmentPlatformBinding
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
 * @date 2022/5/31
 * 公众号fragment
 */
class PlatformFragment : BaseFragment<PlatformViewModel, FragmentPlatformBinding>() {


    //界面状态管理
    private lateinit var loadSir: LoadService<Any>

    /** fragment集合 */
    private val fragments: ArrayList<Fragment> by lazy { arrayListOf() }

    /** 分类集合 */
    private val classifyList: ArrayList<String> by lazy { arrayListOf() }


    override fun initView(savedInstanceState: Bundle?) {
        loadSir = loadServiceInit(mViewBind.contentLayout.viewPager) {
            //重试的时候触发的操作
            loadSir.showLoading()
            mViewModel.getPlatformTitleData()
        }

        mViewBind.contentLayout.let {
            it.viewPager.init(this, fragments)
            it.magicIndicator.bindViewPager2(it.viewPager, classifyList)
        }

    }

    override fun lazyLoadData() {
        loadSir.showLoading()
        mViewModel.getPlatformTitleData()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun createObserver() {
        mViewModel.classifyData.observe(viewLifecycleOwner) { data ->
            parseState(data, {
                classifyList.addAll(it.map { it.name })
                it.forEach { classify ->
                    fragments.add(PlatformChildFragment.newInstance(classify.id))
                }
                mViewBind.contentLayout.let {
                    it.magicIndicator.navigator.notifyDataSetChanged()
                    it.viewPager.adapter?.notifyDataSetChanged()
                    it.viewPager.offscreenPageLimit = fragments.size
                }
                loadSir.showSuccess()
            }, {
                //失败
                loadSir.setErrorText(it.errorMsg)
                loadSir.showCallback(ErrorCallBack::class.java)
            })
        }
    }
}