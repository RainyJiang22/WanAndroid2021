package com.base.wanandroid.ui.share

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import cn.nekocode.rxlifecycle.LifecycleEvent
import cn.nekocode.rxlifecycle.compact.RxLifecycleCompact
import com.base.wanandroid.R
import com.base.wanandroid.application.eventViewModel
import com.base.wanandroid.base.BaseActivity
import com.base.wanandroid.base.BaseFragment
import com.base.wanandroid.databinding.ActivityShareArticleBinding
import com.base.wanandroid.utils.InputTextManager
import com.base.wanandroid.utils.RxTransformer
import com.base.wanandroid.utils.hideSoftKeyboard
import com.blankj.utilcode.util.ToastUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.hgj.jetpackmvvm.ext.nav
import me.hgj.jetpackmvvm.ext.parseState

/**
 * @author jiangshiyu
 * @date 2022/6/16
 * 分享文章界面
 */
class ShareArticleActivity : BaseActivity<ShareViewModel, ActivityShareArticleBinding>() {


    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.titleBar.leftView?.setOnClickListener {
            finishAfterTransition()
        }
        //联动分享按钮和标题链接文本框
        mViewBind.btnShare.let {
            InputTextManager.with(this)
                .addView(mViewBind.etShareTitle)
                .addView(mViewBind.etShareLink)
                .setMain(it)
                .build()
        }

        mViewBind.btnShare.setOnClickListener {
            //隐藏输入法
            hideSoftKeyboard(this)
            val etShareTitle = mViewBind.etShareTitle.text.toString()
            val etShareLink = mViewBind.etShareLink.text.toString()
            lifecycleScope.launch {
                delay(2000)
                mViewModel.addArticle(etShareTitle, etShareLink)
            }
        }
    }

    override fun createObserver() {

        mViewModel.addData.observe(this) { resultState ->
            parseState(resultState, {
                //分享成功
                mViewBind.btnShare.showSucceed()
                ToastUtils.showShort(getString(R.string.share_succeed))
                eventViewModel.shareArticleEvent.value = true
                ActivityCompat.finishAfterTransition(this@ShareArticleActivity)
            }, {
                //分享按钮显示失败
                mViewBind.btnShare.showError(2000)
                //弹出错误信息吐司
                ToastUtils.showShort(it.message)
                //标题输入框加载动画效果
                mViewBind.etShareTitle.startAnimation(
                    AnimationUtils.loadAnimation(
                        this@ShareArticleActivity,
                        R.anim.shake_anim
                    )
                )
                //链接输入框加载动画效果
                mViewBind.etShareLink.startAnimation(
                    AnimationUtils.loadAnimation(
                        this@ShareArticleActivity,
                        R.anim.shake_anim
                    )
                )
            })

        }
    }
}