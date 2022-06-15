package com.base.wanandroid.ui.collect

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import cn.nekocode.rxlifecycle.LifecycleEvent
import cn.nekocode.rxlifecycle.compact.RxLifecycleCompact
import com.base.wanandroid.R
import com.base.wanandroid.base.BaseActivity
import com.base.wanandroid.databinding.ActivityCollectBinding
import com.base.wanandroid.ui.adapter.CollectAdapter
import com.base.wanandroid.utils.RxTransformer
import com.base.wanandroid.utils.lifecycleOwner
import com.blankj.utilcode.util.ToastUtils
import com.drake.brv.PageRefreshLayout
import com.youth.banner.util.LogUtils
import kotlinx.coroutines.launch

/**
 * @author jiangshiyu
 * @date 2022/6/14
 * 收藏界面
 */
class CollectActivity : BaseActivity<ActivityCollectBinding, CollectViewModel>() {

    private val adapter: CollectAdapter by lazy { CollectAdapter(this, viewModel) }

    private var first = true


    override fun onBundle(bundle: Bundle) {
    }

    override fun init(savedInstanceState: Bundle?) {
        binding?.titleBar?.leftView?.setOnClickListener {
            finishAfterTransition()
        }
        binding?.titleBar?.title = getString(R.string.my_collect)
        onRefresh()
    }


    private fun onRefresh() {
        binding?.rv?.adapter = adapter
        binding?.page?.onRefresh {
            lifecycleScope.launch {
                viewModel.getCollectList(index)
                    .compose(
                        RxLifecycleCompact.bind(this@CollectActivity)
                            .disposeObservableWhen(LifecycleEvent.DESTROY)
                    )
                    .compose(RxTransformer.async())
                    .subscribe({
                        if (first && it.data.datas.isEmpty()) {
                            showEmpty()
                        } else {
                            first = false
                            index += if (index == 0) {
                                adapter.setList(it.data.datas)
                                1
                            } else {
                                if (it.data.datas.isNullOrEmpty()) {
                                    //没有更多数据，结束动画，显示内容(没有更多数据)
                                    showContent(false)
                                    return@subscribe
                                }
                                //添加数据
                                adapter.addData(it.data.datas)
                                1
                            }
                        }
                    }, {
                        showError()
                        Log.e("CollectActivity", "onError: ${it.message}")
                    }).lifecycleOwner(this@CollectActivity)
            }
        }?.autoRefresh()
    }

    override fun onResume() {
        super.onResume()
        PageRefreshLayout.startIndex = 0
    }
}