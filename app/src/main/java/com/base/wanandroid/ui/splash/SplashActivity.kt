package com.base.wanandroid.ui.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import cn.nekocode.rxlifecycle.LifecycleEvent
import cn.nekocode.rxlifecycle.compact.RxLifecycleCompact
import com.base.wanandroid.base.BaseActivity
import com.base.wanandroid.databinding.ActivitySplashBinding
import com.base.wanandroid.ui.MainActivity
import com.base.wanandroid.ui.user.UserViewModel
import com.base.wanandroid.utils.AppConfig
import com.base.wanandroid.utils.RxTransformer
import com.base.wanandroid.utils.lifecycleOwner
import com.drake.serialize.intent.openActivity
import com.gyf.immersionbar.ktx.immersionBar

/**
 * @author jiangshiyu
 * @date 2022/6/22
 * 闪屏页
 */
@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<UserViewModel, ActivitySplashBinding>() {

    private val alphaAnimation: AlphaAnimation by lazy { AlphaAnimation(0.3F, 1.0F) }

    override fun initView(savedInstanceState: Bundle?) {
        immersionBar {
            this.statusBarDarkFont(true)
        }
        alphaAnimation.run {
            duration = 1200
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(p0: Animation?) {}

                override fun onAnimationEnd(p0: Animation?) {
                    handleToMain()
                }

                override fun onAnimationStart(p0: Animation?) {

                }
            })
        }

        mViewBind.layoutSplash.startAnimation(alphaAnimation)
    }

    private fun handleToMain() {
        openActivity<MainActivity>()
        finishAfterTransition()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}