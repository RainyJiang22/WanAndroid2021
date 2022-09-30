package com.base.wanandroid.ui.integral

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import cn.nekocode.rxlifecycle.LifecycleEvent
import cn.nekocode.rxlifecycle.compact.RxLifecycleCompact
import com.base.wanandroid.R
import com.base.wanandroid.application.appViewModel
import com.base.wanandroid.base.BaseActivity
import com.base.wanandroid.databinding.ActivityLeaderboardBinding
import com.base.wanandroid.ext.init
import com.base.wanandroid.ext.initFooter
import com.base.wanandroid.ext.loadListData
import com.base.wanandroid.ext.loadServiceInit
import com.base.wanandroid.ext.showLoading
import com.base.wanandroid.ui.adapter.LeaderBoardAdapter
import com.base.wanandroid.utils.AppConfig
import com.base.wanandroid.utils.RxTransformer
import com.base.wanandroid.utils.initFloatBtn
import com.base.wanandroid.utils.lifecycleOwner
import com.base.wanandroid.widget.recyclerview.SpaceItemDecoration
import com.blankj.utilcode.util.ConvertUtils
import com.drake.brv.PageRefreshLayout
import com.kingja.loadsir.core.LoadService
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.coroutines.launch

/**
 * @author jiangshiyu
 * @date 2022/6/15
 * 积分排行页
 */
class LeaderBoardActivity : BaseActivity<IntegralViewModel, ActivityLeaderboardBinding>() {


    private val adapter by lazy { LeaderBoardAdapter() }

    private lateinit var loadSir: LoadService<Any>

    override fun initView(savedInstanceState: Bundle?) {

        mViewBind.titleBar.leftView?.setOnClickListener {
            finishAfterTransition()
        }
        mViewBind.integralMe.visibility = View.GONE

        loadSir = loadServiceInit(mViewBind.swipeRefresh) {
            //重试
            loadSir.showLoading()
            mViewModel.getIntegralData(true)
        }

        //初始化recyclerView
        mViewBind.rv.init(LinearLayoutManager(this), adapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
            it.initFooter {
                //触发加载更多时请求数据
                mViewModel.getIntegralData(false)
            }
            //初始化FloatingActionButton
            it.initFloatBtn(mViewBind.fab)
        }

        mViewBind.swipeRefresh.init {
            mViewModel.getIntegralData(true)
        }
    }

    override fun onResume() {
        super.onResume()
        loadSir.showLoading()
        mViewModel.getIntegralData(true)
    }


    override fun createObserver() {
        mViewModel.integralDataState.observe(this) {
            loadListData(it, adapter, loadSir, mViewBind.rv, mViewBind.swipeRefresh)
        }
    }


}