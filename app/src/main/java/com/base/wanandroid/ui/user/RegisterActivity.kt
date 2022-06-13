package com.base.wanandroid.ui.user

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.lifecycle.lifecycleScope
import cn.nekocode.rxlifecycle.LifecycleEvent
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
class RegisterActivity : BaseActivity<ActivityRegisterBinding, UserViewModel>() {
    override fun onBundle(bundle: Bundle) {
    }

    override fun init(savedInstanceState: Bundle?) {
        //标题栏返回按钮关闭页面
        binding?.titleBar?.leftView?.setOnClickListener { finishAfterTransition() }
        //联动注册按钮和账号密码输入框
        binding?.btnRegister?.let {
            InputTextManager.with(this)
                .addView(binding?.etUsername)
                .addView(binding?.etPassword)
                .addView(binding?.etRePassword)
                .setMain(it)
                .build()
        }
        //注册按钮点击事件
        binding?.btnRegister?.setOnClickListener {
            //校验两次密码是否一致
            if (binding?.etPassword?.text.toString() != binding?.etRePassword?.text.toString()) {
                binding?.etPassword?.startAnimation(
                    AnimationUtils.loadAnimation(
                        this,
                        R.anim.shake_anim
                    )
                )
                binding?.etRePassword?.startAnimation(
                    AnimationUtils.loadAnimation(
                        this,
                        R.anim.shake_anim
                    )
                )
                binding?.btnRegister?.showError(2000)
                ToastUtils.showShort(getString(R.string.re_enter_password))
                return@setOnClickListener
            }
            //隐藏输入法
            hideSoftKeyboard(this)
            lifecycleScope.launch {
                val username = binding?.etUsername?.text.toString()
                val etPassword = binding?.etPassword?.text.toString()
                val etRePassword = binding?.etRePassword?.text.toString()
                delay(1500)
                //先获取用户注册信息数据
                viewModel.getRegisterUserInfo(username, etPassword, etRePassword)
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
                        binding?.btnRegister?.showError(2000)
                        //弹出错误信息吐司
                        ToastUtils.showShort(it.message)
                        //账号输入框加载动画效果
                        binding?.etUsername?.startAnimation(
                            AnimationUtils.loadAnimation(
                                this@RegisterActivity,
                                R.anim.shake_anim
                            )
                        )
                        //密码输入框加载动画效果
                        binding?.etPassword?.startAnimation(
                            AnimationUtils.loadAnimation(
                                this@RegisterActivity,
                                R.anim.shake_anim
                            )
                        )
                        //确认密码输入框加载动画效果
                        binding?.etRePassword?.startAnimation(
                            AnimationUtils.loadAnimation(
                                this@RegisterActivity,
                                R.anim.shake_anim
                            )
                        )
                    })

            }
            //登陆文本点击事件
            binding?.tvLogin?.setOnClickListener {
                openActivity<LoginActivity>()
                finish()
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }


        }
    }
}