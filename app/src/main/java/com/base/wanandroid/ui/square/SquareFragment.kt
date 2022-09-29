package com.base.wanandroid.ui.square

import androidx.fragment.app.Fragment
import com.base.wanandroid.base.BaseFragment1
import com.base.wanandroid.databinding.FragmentSquareBinding
import com.base.wanandroid.ui.answer.InquiryAnswerFragment
import com.base.wanandroid.ui.navigation.NavigationFragment
import com.base.wanandroid.ui.tree.TreeFragment
import com.base.wanandroid.utils.bindViewPager2
import com.base.wanandroid.utils.init

/**
 * @author jiangshiyu
 * @date 2022/5/31
 * 广场fragment
 */
class SquareFragment : BaseFragment1<SquareViewModel, FragmentSquareBinding>() {


    /** 分类集合 */
    private val classifyList: ArrayList<String> by lazy { arrayListOf("广场", "每日一问", "体系", "导航") }

    //子项目体系fragment集合
    private val fragments: ArrayList<Fragment> by lazy { arrayListOf() }


    init {
        fragments.add(SquareChildFragment())
        fragments.add(InquiryAnswerFragment())
        fragments.add(TreeFragment())
        fragments.add(NavigationFragment())
    }


    override fun lazyLoadData() {
        mViewBind.contentLayout.let {
            it.viewPager.init(this, fragments)
            it.magicIndicator.bindViewPager2(it.viewPager, classifyList)
            it.viewPager.offscreenPageLimit = fragments.size
        }
    }

}