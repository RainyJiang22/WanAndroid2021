package com.base.wanandroid.ui.integral

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import cn.nekocode.rxlifecycle.LifecycleEvent
import cn.nekocode.rxlifecycle.compact.RxLifecycleCompact
import com.base.wanandroid.R
import com.base.wanandroid.base.BaseActivity
import com.base.wanandroid.databinding.ActivityIntegralBinding
import com.base.wanandroid.ui.adapter.IntegralAdapter
import com.base.wanandroid.ui.web.WebActivity
import com.base.wanandroid.utils.AnimatorUtil
import com.base.wanandroid.utils.AppConfig
import com.base.wanandroid.utils.RxTransformer
import com.drake.brv.PageRefreshLayout
import com.example.wanAndroid.widget.decoration.RecyclerViewItemDecoration
import kotlinx.coroutines.launch

/**
 * @author jiangshiyu
 * @date 2022/6/15
 * 我的积分页
 */
class IntegralActivity : BaseActivity<ActivityIntegralBinding, IntegralViewModel>() {


    private var first = true

    private val integralAdapter by lazy {
        IntegralAdapter().apply {
            setDiffCallback(IntegralDiffCallBack())
        }
    }

    override fun onBundle(bundle: Bundle) {
    }

    override fun init(savedInstanceState: Bundle?) {

        //带动画显示个人积分
        binding?.myIntegral?.let { AnimatorUtil.doIntAnim(it, AppConfig.CoinCount.toInt(), 1000) }

        binding?.titleBar?.apply {
            leftView?.setOnClickListener {
                finishAfterTransition()
            }
            rightView?.setOnClickListener {
                WebActivity.start(this@IntegralActivity, getString(R.string.integral_help))
            }
        }

        binding?.rv?.apply {
            adapter = integralAdapter
            addItemDecoration(RecyclerViewItemDecoration(this@IntegralActivity))
        }
        PageRefreshLayout.startIndex = 0
        onRefresh()
    }

    private fun onRefresh() {
        binding?.page?.onRefresh {
            lifecycleScope.launch {
                viewModel.getIntegralList(index)
                    .compose(
                        RxLifecycleCompact.bind(this@IntegralActivity)
                            .disposeObservableWhen(LifecycleEvent.DESTROY)
                    )
                    .compose(RxTransformer.async())
                    .subscribe({
                        if (first && it.data.datas.isEmpty()) {
                            showEmpty()
                        } else {
                            first = false
                            index += if (index == 0) {
                                integralAdapter.setList(it.data.datas)
                                1
                            } else {
                                if (it.data.datas.isNullOrEmpty()) {
                                    //没有更多数据，结束动画，显示内容(没有更多数据)
                                    showContent(false)
                                    return@subscribe
                                }
                                //添加数据
                                integralAdapter.addData(it.data.datas)
                                1
                            }
                        }
                        showContent(true)
                    }, {
                        showError()
                    })
            }
        }?.autoRefresh()

    }

}