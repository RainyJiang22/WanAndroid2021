package com.base.wanandroid.ui.share

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import cn.nekocode.rxlifecycle.LifecycleEvent
import cn.nekocode.rxlifecycle.compact.RxLifecycleCompact
import com.base.wanandroid.base.BaseActivity
import com.base.wanandroid.databinding.ActivityShareBinding
import com.base.wanandroid.ui.adapter.ShareAdapter
import com.base.wanandroid.utils.RxTransformer
import com.base.wanandroid.utils.initFloatBtn
import com.base.wanandroid.widget.layout.SwipeItemLayout
import com.drake.brv.PageRefreshLayout
import com.example.wanAndroid.widget.decoration.SpaceItemDecoration
import kotlinx.coroutines.launch

/**
 * @author jiangshiyu
 * @date 2022/6/16
 * 我的分享
 */
class ShareActivity : BaseActivity<ShareViewModel,ActivityShareBinding>() {


    private val adapter by lazy { ShareAdapter(this, mViewModel) }

    private var first = true

    private lateinit var startShareLauncher: ActivityResultLauncher<Intent>


    override fun initView(savedInstanceState: Bundle?) {
        startShareLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    mViewBind.page.refresh()
                }
            }

        mViewBind.titleBar.let {
            it.leftView.setOnClickListener { finishAfterTransition() }
            it.rightView.setOnClickListener {
                startShareLauncher.launch(Intent(this, ShareArticleActivity::class.java))
            }
        }
        mViewBind.titleBar.leftView?.setOnClickListener { finish() }
        //标题栏右侧图标打开分享文章页面，获取返回结果，增加一条数据
        mViewBind.titleBar.rightView?.setOnClickListener {
            startShareLauncher.launch(Intent(this, ShareArticleActivity::class.java))
        }
        PageRefreshLayout.startIndex = 1
        initAdapter()
        onRefresh()
    }


    private fun initAdapter() {
        mViewBind.rv.apply {
            this.adapter = adapter
            addItemDecoration(SpaceItemDecoration(this@ShareActivity))
            //设置RecycleView的侧滑监听器
            addOnItemTouchListener(SwipeItemLayout.OnSwipeItemTouchListener(this@ShareActivity))
            initFloatBtn(mViewBind.fab)
        }
    }

    private fun onRefresh() {
        mViewBind.page.onRefresh {
            lifecycleScope.launch {
                mViewModel.getShareList(index)
                    .compose(
                        RxLifecycleCompact.bind(this@ShareActivity)
                            .disposeObservableWhen(LifecycleEvent.DESTROY)
                    )
                    .compose(RxTransformer.async())
                    .subscribe({
                        if (first && it.data.shareArticles.datas.isEmpty()) {
                            //如果第一次切换且数据为空显示空缺省页
                            showEmpty()
                        } else {
                            //设置初次创建页面为否
                            first = false
                            index += if (index == 1) { //下拉刷新
                                //设置数据
                                adapter.setList(it.data.shareArticles.datas)
                                //翻页
                                1
                            } else { //上拉加载更多
                                if (it.data.shareArticles.datas.isEmpty()) {
                                    //没有更多数据，结束动画，显示内容(没有更多数据)
                                    showContent(false)
                                    return@subscribe
                                }
                                //添加数据
                                adapter.addData(it.data.shareArticles.datas)
                                //翻页
                                1
                            }
                            showContent(true)
                        }
                    }, {
                        showError(it.message)
                    })
            }
        }.autoRefresh()
    }


}