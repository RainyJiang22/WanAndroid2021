package com.base.wanandroid.ui

import android.os.Bundle
import com.base.wanandroid.R
import com.base.wanandroid.base.BaseFragment
import com.base.wanandroid.databinding.FragmentMainBinding
import com.base.wanandroid.ext.init
import com.base.wanandroid.ext.initMain
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * @author jiangshiyu
 * @date 2022/10/9
 */
class MainFragment : BaseFragment<BaseViewModel, FragmentMainBinding>() {


    companion object {
        const val TAG = "Main"
    }


    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.run {
            //初始化viewpager2
            mainViewpager.initMain(this@MainFragment)
            //初始化bottomNavigation
            bottomNavigation.init {
                when (it) {
                    R.id.page_home -> mainViewpager.setCurrentItem(0, false)
                    R.id.page_tree -> mainViewpager.setCurrentItem(1, false)
                    R.id.page_square -> mainViewpager.setCurrentItem(2, false)
                    R.id.page_platform -> mainViewpager.setCurrentItem(3, false)
                    R.id.page_mine -> mainViewpager.setCurrentItem(4, false)
                }
            }
        }
    }
}