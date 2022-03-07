package com.base.wanandroid.ui


import android.os.Bundle
import com.base.wanandroid.R
import com.base.wanandroid.base.BaseActivity
import com.base.wanandroid.databinding.ActivityMainBinding
import com.base.wanandroid.ui.home.HomeFragment
import com.base.wanandroid.ui.tree.TreeFragment
import com.base.wanandroid.utils.replaceFragment
import com.photoroom.editor.base.EmptyViewModel

class MainActivity : BaseActivity<ActivityMainBinding, EmptyViewModel>() {

    companion object {
        const val TAG = "MAIN"

        const val HOME_TAG = "HOME"
        const val TREE_TAG = "TREE"
    }


    private val fragment by lazy { arrayOf(HomeFragment(), TreeFragment()) }

    override fun onBundle(bundle: Bundle) {

    }

    override fun init(savedInstanceState: Bundle?) {

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
                else -> {
                    false
                }
            }
        }

    }

}