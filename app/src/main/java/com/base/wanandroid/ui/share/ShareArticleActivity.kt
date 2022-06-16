package com.base.wanandroid.ui.share

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import cn.nekocode.rxlifecycle.LifecycleEvent
import cn.nekocode.rxlifecycle.compact.RxLifecycleCompact
import com.base.wanandroid.R
import com.base.wanandroid.base.BaseActivity
import com.base.wanandroid.databinding.ActivityShareArticleBinding
import com.base.wanandroid.utils.InputTextManager
import com.base.wanandroid.utils.RxTransformer
import com.base.wanandroid.utils.hideSoftKeyboard
import com.blankj.utilcode.util.ToastUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @author jiangshiyu
 * @date 2022/6/16
 * 分享文章界面
 */
class ShareArticleActivity : BaseActivity<ActivityShareArticleBinding, ShareViewModel>() {
    override fun onBundle(bundle: Bundle) {
    }

    override fun init(savedInstanceState: Bundle?) {
        binding?.titleBar?.leftView?.setOnClickListener { finishAfterTransition() }
        //联动分享按钮和标题链接文本框
        binding?.btnShare?.let {
            InputTextManager.with(this)
                .addView(binding?.etShareTitle)
                .addView(binding?.etShareLink)
                .setMain(it)
                .build()
        }

        binding?.btnShare?.setOnClickListener {
            //隐藏输入法
            hideSoftKeyboard(this)
            val etShareTitle = binding?.etShareTitle?.text.toString()
            val etShareLink = binding?.etShareLink?.text.toString()
            lifecycleScope.launch {
                delay(2000)
                viewModel.shareCurrentArticle(etShareTitle, etShareLink)
                    .compose(
                        RxLifecycleCompact.bind(this@ShareArticleActivity)
                            .disposeObservableWhen(LifecycleEvent.DESTROY)
                    )
                    .compose(RxTransformer.async())
                    .subscribe({
                        binding?.btnShare?.showSucceed()
                        ToastUtils.showShort(getString(R.string.share_succeed))
                        setResult(RESULT_OK)
                        ActivityCompat.finishAfterTransition(this@ShareArticleActivity)
                    }, {
                        //分享按钮显示失败
                        binding?.btnShare?.showError(2000)
                        //弹出错误信息吐司
                        ToastUtils.showShort(it.message)
                        //标题输入框加载动画效果
                        binding?.etShareTitle?.startAnimation(
                            AnimationUtils.loadAnimation(
                                this@ShareArticleActivity,
                                R.anim.shake_anim
                            )
                        )
                        //链接输入框加载动画效果
                        binding?.etShareLink?.startAnimation(
                            AnimationUtils.loadAnimation(
                                this@ShareArticleActivity,
                                R.anim.shake_anim
                            )
                        )
                    })
            }
        }

    }
}