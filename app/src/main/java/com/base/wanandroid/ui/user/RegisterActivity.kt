package com.base.wanandroid.ui.user

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.lifecycle.lifecycleScope
import com.base.wanandroid.R
import com.base.wanandroid.base.BaseActivity
import com.base.wanandroid.databinding.ActivityRegisterBinding
import com.base.wanandroid.utils.AppConfig
import com.base.wanandroid.utils.InputTextManager
import com.base.wanandroid.utils.RxTransformer
import com.base.wanandroid.utils.hideSoftKeyboard
import com.blankj.utilcode.util.ToastUtils
import com.drake.serialize.intent.openActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
                mViewModel.getRegisterUserInfo(username, etPassword, etRePassword)
                    .compose(RxTransformer.async())
                    .subscribe({
                        AppConfig.UserName = it.userInfoResponse?.username ?: "Jacky"
                        AppConfig.PassWord = etPassword
                        AppConfig.Level = it.coinInfoResponse?.level.toString()
                        AppConfig.Rank = it.coinInfoResponse?.rank ?: "1"
                        AppConfig.CoinCount = it.coinInfoResponse?.coinCount.toString()

                        finishAfterTransition()
                        openActivity<LoginActivity>()
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
            //登陆文本点击事件
            mViewBind.tvLogin.setOnClickListener {
                openActivity<LoginActivity>()
                finish()
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }


        }
    }
}