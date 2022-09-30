package com.base.wanandroid.ui.integral

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import cn.nekocode.rxlifecycle.LifecycleEvent
import cn.nekocode.rxlifecycle.compact.RxLifecycleCompact
import com.base.wanandroid.R
import com.base.wanandroid.application.appViewModel
import com.base.wanandroid.base.BaseActivity
import com.base.wanandroid.databinding.ActivityIntegralBinding
import com.base.wanandroid.ext.init
import com.base.wanandroid.ext.initFooter
import com.base.wanandroid.ext.loadListData
import com.base.wanandroid.ext.loadServiceInit
import com.base.wanandroid.ext.showLoading
import com.base.wanandroid.ui.adapter.IntegralAdapter
import com.base.wanandroid.ui.web.WebActivity
import com.base.wanandroid.utils.AnimatorUtil
import com.base.wanandroid.utils.AppConfig
import com.base.wanandroid.utils.RxTransformer
import com.base.wanandroid.utils.initFloatBtn
import com.base.wanandroid.widget.recyclerview.SpaceItemDecoration
import com.blankj.utilcode.util.ConvertUtils
import com.drake.brv.PageRefreshLayout
import com.example.wanAndroid.widget.decoration.RecyclerViewItemDecoration
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import kotlinx.coroutines.launch

/**
 * @author jiangshiyu
 * @date 2022/6/15
 * 我的积分页
 */
class IntegralActivity : BaseActivity<IntegralViewModel, ActivityIntegralBinding>() {


    private val integralAdapter by lazy {
        IntegralAdapter().apply {
            setDiffCallback(IntegralDiffCallBack())
        }
    }

    //界面状态管理
    private lateinit var loadSir: LoadService<Any>

    override fun initView(savedInstanceState: Bundle?) {

        //带动画显示个人积分
        mViewBind.myIntegral.let { AnimatorUtil.doIntAnim(it, AppConfig.CoinCount.toInt(), 1000) }

        mViewBind.titleBar.apply {
            leftView?.setOnClickListener {
                finishAfterTransition()
            }
            rightView?.setOnClickListener {
                WebActivity.start(this@IntegralActivity, getString(R.string.integral_help))
            }
        }
        loadSir = loadServiceInit(mViewBind.swipeRefresh) {
            loadSir.showLoading()
            mViewModel.getIntegralHistoryData(true)
        }

        //初始化recyclerView
        mViewBind.rv.init(LinearLayoutManager(this), integralAdapter).let {
            it.initFooter {
                //触发加载更多时请求数据
                mViewModel.getIntegralHistoryData(false)
            }
            //初始化FloatingActionButton
            it.initFloatBtn(mViewBind.fab)
        }
    }

    override fun onResume() {
        super.onResume()
        loadSir.showLoading()
        mViewModel.getIntegralHistoryData(true)
    }


    override fun createObserver() {
        mViewModel.integralHistoryDataState.observe(this) {
            loadListData(it, integralAdapter, loadSir, mViewBind.rv, mViewBind.swipeRefresh)
        }
    }

}