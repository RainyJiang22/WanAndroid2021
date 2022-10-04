package com.base.wanandroid.ui.user

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.lifecycle.lifecycleScope
import com.base.wanandroid.R
import com.base.wanandroid.application.appViewModel
import com.base.wanandroid.base.BaseActivity
import com.base.wanandroid.databinding.ActivityRegisterBinding
import com.base.wanandroid.ui.MainActivity
import com.base.wanandroid.utils.CacheUtil
import com.base.wanandroid.utils.InputTextManager
import com.base.wanandroid.utils.hideSoftKeyboard
import com.blankj.utilcode.util.ToastUtils
import com.drake.serialize.intent.openActivity
import com.rainy.easybus.EventBusInitializer
import com.rainy.easybus.extention.postEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.hgj.jetpackmvvm.ext.parseState

/**
 * @author jiangshiyu
 * @date 2022/6/10
 */
class RegisterActivity : BaseActivity<UserViewModel, ActivityRegisterBinding>() {


    override fun initView(savedInstanceState: Bundle?) {
        //标题栏返回按钮关闭页面
        mViewBind.titleBar.leftView?.setOnClickListener { finishAfterTransition() }
        //联动注册按钮和账号密码输入框
        mViewBind.btnRegister.let {
            InputTextManager.with(this)
                .addView(mViewBind.etUsername)
                .addView(mViewBind.etPassword)
                .addView(mViewBind.etRePassword)
                .setMain(it)
                .build()
        }
        //注册按钮点击事件
        mViewBind.btnRegister.setOnClickListener {
            //校验两次密码是否一致
            if (mViewBind.etPassword.text.toString() != mViewBind.etRePassword.text.toString()) {
                mViewBind.etPassword.startAnimation(
                    AnimationUtils.loadAnimation(
                        this,
                        R.anim.shake_anim
                    )
                )
                mViewBind.etRePassword.startAnimation(
                    AnimationUtils.loadAnimation(
                        this,
                        R.anim.shake_anim
                    )
                )
                mViewBind.btnRegister.showError(2000)
                ToastUtils.showShort(getString(R.string.re_enter_password))
                return@setOnClickListener
            }
            //隐藏输入法
            hideSoftKeyboard(this)
            lifecycleScope.launch {
                val username = mViewBind.etUsername.text.toString()
                val etPassword = mViewBind.etPassword.text.toString()
                val etRePassword = mViewBind.etRePassword.text.toString()
                delay(1500)
                //先获取用户注册信息数据
                mViewModel.registerAndLogin(username, etPassword)
            }
            //登陆文本点击事件
            mViewBind.tvLogin.setOnClickListener {
                openActivity<LoginActivity>()
                finish()
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }


        }
    }

    override fun createObserver() {
        mViewModel.loginResult.observe(this) { resultState ->
            parseState(resultState, {
                CacheUtil.setIsLogin(true)
                CacheUtil.setUser(it)
                postEvent(it, false)
                appViewModel.userInfo.value = it
                finishAfterTransition()
                openActivity<MainActivity>()
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }, {
                //注册按钮显示失败
                mViewBind.btnRegister.showError(2000)
                //弹出错误信息吐司
                ToastUtils.showShort(it.message)
                //账号输入框加载动画效果
                mViewBind.etUsername.startAnimation(
                    AnimationUtils.loadAnimation(
                        this@RegisterActivity,
                        R.anim.shake_anim
                    )
                )
                //密码输入框加载动画效果
                mViewBind.etPassword.startAnimation(
                    AnimationUtils.loadAnimation(
                        this@RegisterActivity,
                        R.anim.shake_anim
                    )
                )
                //确认密码输入框加载动画效果
                mViewBind.etRePassword.startAnimation(
                    AnimationUtils.loadAnimation(
                        this@RegisterActivity,
                        R.anim.shake_anim
                    )
                )
            })
        }
    }
}