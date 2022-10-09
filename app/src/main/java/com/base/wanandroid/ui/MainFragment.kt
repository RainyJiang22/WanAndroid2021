package com.base.wanandroid.ui

import android.os.Bundle
import com.base.wanandroid.R
import com.base.wanandroid.base.BaseFragment
import com.base.wanandroid.databinding.FragmentMainBinding
import com.base.wanandroid.ui.home.HomeFragment
import com.base.wanandroid.ui.mine.MineFragment
import com.base.wanandroid.ui.platform.PlatformFragment
import com.base.wanandroid.ui.project.ProjectFragment
import com.base.wanandroid.ui.square.SquareFragment
import com.base.wanandroid.utils.replaceFragment
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * @author jiangshiyu
 * @date 2022/10/9
 */
class MainFragment : BaseFragment<BaseViewModel, FragmentMainBinding>() {


    companion object {

        const val HOME_TAG = "HOME"
        const val TREE_TAG = "TREE"
        const val SQUARE_TAG = "SQUARE"
        const val PLATFORM_TAG = "PLATFORM"
        const val MINE_TAG = "MINE"
    }

    private val fragment by lazy {
        arrayOf(
            HomeFragment(),
            ProjectFragment(),
            SquareFragment(),
            PlatformFragment(),
            MineFragment()
        )
    }

    override fun initView(savedInstanceState: Bundle?) {

        replaceFragment(fragment[0], HOME_TAG)
        mViewBind.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_home -> {
                    replaceFragment(fragment[0], HOME_TAG)
                    true
                }
                R.id.page_tree -> {
                    replaceFragment(fragment[1], TREE_TAG)
                    true
                }
                R.id.page_square -> {
                    replaceFragment(fragment[2], SQUARE_TAG)
                    true
                }
                R.id.page_platform -> {
                    replaceFragment(fragment[3], PLATFORM_TAG)
                    true
                }
                R.id.page_mine -> {
                    replaceFragment(fragment[4], MINE_TAG)
                    true
                }
                else -> {
                    false
                }
            }
        }
    }
}