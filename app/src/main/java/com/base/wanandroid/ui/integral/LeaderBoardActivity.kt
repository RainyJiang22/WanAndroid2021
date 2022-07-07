package com.base.wanandroid.ui.integral

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import cn.nekocode.rxlifecycle.LifecycleEvent
import cn.nekocode.rxlifecycle.compact.RxLifecycleCompact
import com.base.wanandroid.R
import com.base.wanandroid.base.BaseActivity
import com.base.wanandroid.databinding.ActivityLeaderboardBinding
import com.base.wanandroid.ui.adapter.LeaderBoardAdapter
import com.base.wanandroid.utils.AppConfig
import com.base.wanandroid.utils.RxTransformer
import com.base.wanandroid.utils.initFloatBtn
import com.base.wanandroid.utils.lifecycleOwner
import com.drake.brv.PageRefreshLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author jiangshiyu
 * @date 2022/6/15
 * 积分排行页
 */
class LeaderBoardActivity : BaseActivity<ActivityLeaderboardBinding, IntegralViewModel>() {

    private var first = true

    private val adapter by lazy { LeaderBoardAdapter() }

    override fun onBundle(bundle: Bundle) {

    }

    override fun init(savedInstanceState: Bundle?) {

        binding?.titleBar?.leftView?.setOnClickListener {
            finishAfterTransition()
        }
        if (AppConfig.UserName.isNotEmpty()) {
            binding?.integralMyRank?.text = AppConfig.Rank
            binding?.integralMyName?.text = AppConfig.UserName
            binding?.integralMyLv?.text = getString(R.string.integral_my_lv, AppConfig.Level)
            binding?.integralMyCount?.text = AppConfig.CoinCount
        } else {
            binding?.integralMe?.visibility = View.GONE
        }
        PageRefreshLayout.startIndex = 1
        binding?.rv?.apply {
            this.adapter = adapter
            binding?.fab?.let { initFloatBtn(it) }
        }
        onRefresh()
    }

    private fun onRefresh() {

        binding?.page?.onRefresh {
            lifecycleScope.launch {
                viewModel.getLeaderBoardList(index)
                    .compose(
                        RxLifecycleCompact.bind(this@LeaderBoardActivity)
                            .disposeObservableWhen(LifecycleEvent.DESTROY)
                    )
                    .compose(RxTransformer.async())
                    .subscribe({
                        if (first && it.data.datas.isEmpty()) {
                            showEmpty()
                        } else {
                            first = false
                            index += if (index == 1) {
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
                        showContent(true)
                    }, {
                        showError()
                    }).lifecycleOwner(this@LeaderBoardActivity)

            }

        }?.autoRefresh()
    }


}