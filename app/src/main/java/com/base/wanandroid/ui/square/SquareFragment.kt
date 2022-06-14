package com.base.wanandroid.ui.square

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.base.wanandroid.R
import com.base.wanandroid.base.BaseFragment
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
class SquareFragment : BaseFragment<FragmentSquareBinding, SquareViewModel>() {


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

    override fun onBundle(bundle: Bundle) {

    }

    override fun init(savedInstanceState: Bundle?) {

        binding?.toolbar?.title = getString(R.string.square_fragment)
        binding?.contentLayout?.let {
            it.viewPager.init(this, fragments)
            it.magicIndicator.bindViewPager2(it.viewPager, classifyList)
            it.viewPager.offscreenPageLimit = fragments.size
        }

    }
}