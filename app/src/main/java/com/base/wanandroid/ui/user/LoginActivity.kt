package com.base.wanandroid.ui.user

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.base.wanandroid.R
import com.base.wanandroid.application.appViewModel
import com.base.wanandroid.base.BaseActivity
import com.base.wanandroid.databinding.ActivityLoginBinding
import com.base.wanandroid.utils.AppConfig
import com.base.wanandroid.utils.CacheUtil
import com.base.wanandroid.utils.InputTextManager
import com.base.wanandroid.utils.RxTransformer
import com.base.wanandroid.utils.hideSoftKeyboard
import com.blankj.utilcode.util.ToastUtils
import com.drake.serialize.intent.openActivity
import com.rainy.easybus.extention.postEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.hgj.jetpackmvvm.ext.parseState

/**
 * @author jiangshiyu
 * @date 2022/6/10
 * 登录页面
 */
class LoginActivity : BaseActivity<UserViewModel, ActivityLoginBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.titleBar.leftView?.setOnClickListener {
            finishAfterTransition()
        }

        mViewBind.btnLogin.let {
            InputTextManager.with(this)
                .addView(mViewBind.etUsername)
                .addView(mViewBind.etPassword)
                .setMain(it)
                .build()
        }


        mViewBind.btnLogin.setOnClickListener {
            hideSoftKeyboard(this)
            lifecycleScope.launch {
                delay(1500)
                val editName = mViewBind.etUsername.text.toString()
                val editPassword = mViewBind.etPassword.text.toString()
                mViewModel.loginReq(editName, editPassword)
            }
        }

        //注册文本点击事件
        mViewBind.tvRegister.setOnClickListener {
            openActivity<RegisterActivity>()
            finishAfterTransition()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }


    override fun createObserver() {
        mViewModel.loginResult.observe(this) { resultState ->
            parseState(resultState, {
                CacheUtil.setUser(it)
                CacheUtil.setIsLogin(true)
                postEvent(it, false)
                appViewModel.userInfo.value = it
                mViewBind.btnLogin.showSucceed()
                setResult(Activity.RESULT_OK)
                ActivityCompat.finishAfterTransition(this@LoginActivity)
            }, {
                mViewBind.btnLogin.showError(2000)
                //弹出错误信息吐司
                ToastUtils.showShort(it.message)
                //账号输入框加载动画效果
                mViewBind.etUsername.startAnimation(
                    AnimationUtils.loadAnimation(
                        this@LoginActivity,
                        R.anim.shake_anim
                    )
                )
                //密码输入框加载动画效果
                mViewBind.etPassword.startAnimation(
                    AnimationUtils.loadAnimation(
                        this@LoginActivity,
                        R.anim.shake_anim
                    )
                )
            })
        }
    }

    companion object {
        fun start(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }
}