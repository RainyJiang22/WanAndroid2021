package com.base.wanandroid.ui


import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.navigation.Navigation
import com.base.wanandroid.R
import com.base.wanandroid.base.BaseActivity
import com.base.wanandroid.databinding.ActivityMainBinding
import com.base.wanandroid.ui.home.HomeFragment
import com.base.wanandroid.ui.mine.MineFragment
import com.base.wanandroid.ui.platform.PlatformFragment
import com.base.wanandroid.ui.project.ProjectFragment
import com.base.wanandroid.ui.square.SquareFragment
import com.base.wanandroid.utils.replaceFragment
import com.blankj.utilcode.util.ToastUtils
import com.gyf.immersionbar.ktx.immersionBar
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

class MainActivity : BaseActivity<BaseViewModel, ActivityMainBinding>() {

    companion object {
        const val TAG = "MAIN"

    }


    var exitTime = 0L


    override fun initView(savedInstanceState: Bundle?) {
        immersionBar {
            this.statusBarDarkFont(true)
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val nav = Navigation.findNavController(this@MainActivity,R.id.host_fragment)
                if (nav.currentDestination != null && nav.currentDestination!!.id != R.id.mainFragment) {
                    //如果当前界面不是主页，那么直接调用返回即可
                    nav.navigateUp()
                } else {
                    //是主页
                    if (System.currentTimeMillis() - exitTime > 2000) {
                        ToastUtils.showShort("再按一次退出程序")
                        exitTime = System.currentTimeMillis()
                    } else {
                        finish()
                    }
                }
            }

        })

    }

}