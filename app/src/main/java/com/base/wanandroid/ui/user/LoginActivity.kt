package com.base.wanandroid.ui.user

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.lifecycle.lifecycleScope
import com.base.wanandroid.R
import com.base.wanandroid.base.BaseActivity
import com.base.wanandroid.databinding.ActivityLoginBinding
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
 * 登录页面
 */
class LoginActivity : BaseActivity<ActivityLoginBinding, UserViewModel>() {
    override fun onBundle(bundle: Bundle) {
    }

    override fun init(savedInstanceState: Bundle?) {
        binding?.titleBar?.leftView?.setOnClickListener {
            finishAfterTransition()
        }

        binding?.btnLogin?.let {
            InputTextManager.with(this)
                .addView(binding?.etUsername)
                .addView(binding?.etPassword)
                .setMain(it)
                .build()
        }


        binding?.btnLogin?.setOnClickListener {
            hideSoftKeyboard(this)
            lifecycleScope.launch {
                delay(1500)

                val editName = binding?.etUsername.toString()
                val editPassword = binding?.etPassword.toString()
                viewModel.getLoginUserInfo(editName, editPassword)
                    .compose(RxTransformer.async())
                    .subscribe({

                        AppConfig.UserName = it.userInfoResponse?.username ?: "Jacky"
                        AppConfig.PassWord = editPassword
                        AppConfig.Level = it.coinInfoResponse?.level.toString()
                        AppConfig.Rank = it.coinInfoResponse?.rank ?: "1"
                        AppConfig.CoinCount = it.coinInfoResponse?.coinCount.toString()

                        binding?.btnLogin?.showSucceed()
                        finish()
                    }, {
                        binding?.btnLogin?.showError(2000)
                        //弹出错误信息吐司
                        ToastUtils.showShort(it.message)
                        //账号输入框加载动画效果
                        binding?.etUsername?.startAnimation(
                            AnimationUtils.loadAnimation(
                                this@LoginActivity,
                                R.anim.shake_anim
                            )
                        )
                        //密码输入框加载动画效果
                        binding?.etPassword?.startAnimation(
                            AnimationUtils.loadAnimation(
                                this@LoginActivity,
                                R.anim.shake_anim
                            )
                        )
                    })
            }
        }

        //注册文本点击事件
        binding?.tvRegister?.setOnClickListener {
            openActivity<RegisterActivity>()
            finishAfterTransition()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }


    }
}