package com.base.wanandroid.ui


import android.os.Bundle
import com.base.wanandroid.R
import com.base.wanandroid.base.BaseActivity
import com.base.wanandroid.databinding.ActivityMainBinding
import com.base.wanandroid.ui.home.HomeFragment
import com.base.wanandroid.ui.mine.MineFragment
import com.base.wanandroid.ui.platform.PlatformFragment
import com.base.wanandroid.ui.project.ProjectFragment
import com.base.wanandroid.ui.square.SquareFragment
import com.base.wanandroid.utils.replaceFragment
import com.gyf.immersionbar.ktx.immersionBar
import com.photoroom.editor.base.EmptyViewModel

class MainActivity : BaseActivity<ActivityMainBinding, EmptyViewModel>() {

    companion object {
        const val TAG = "MAIN"

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

    override fun onBundle(bundle: Bundle) {

    }

    override fun init(savedInstanceState: Bundle?) {
        immersionBar {
            this.statusBarDarkFont(true)
        }

        replaceFragment(fragment[0], HOME_TAG)
        binding?.bottomNavigation?.setOnNavigationItemSelectedListener { item ->
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