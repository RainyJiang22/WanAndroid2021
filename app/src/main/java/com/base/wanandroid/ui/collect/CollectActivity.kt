package com.base.wanandroid.ui.collect

import android.os.Bundle
import com.base.wanandroid.R
import com.base.wanandroid.base.BaseActivity
import com.base.wanandroid.bean.CollectResponse
import com.base.wanandroid.bean.base.ApiBaseResponse
import com.base.wanandroid.bean.base.ApiPagerResponse
import com.base.wanandroid.databinding.ActivityCollectBinding
import com.base.wanandroid.ui.adapter.CollectAdapter
import com.base.wanandroid.utils.initFloatBtn
import com.drake.brv.PageRefreshLayout
import com.drake.net.Get
import com.drake.net.utils.scope

/**
 * @author jiangshiyu
 * @date 2022/6/14
 * 收藏界面
 */
class CollectActivity : BaseActivity<CollectViewModel, ActivityCollectBinding>() {

    private val adapter: CollectAdapter by lazy { CollectAdapter(this, mViewModel) }

    private var first = true

    private lateinit var collectData: ApiBaseResponse<ApiPagerResponse<CollectResponse>>


    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.titleBar.leftView?.setOnClickListener {
            finishAfterTransition()
        }
        mViewBind.titleBar.title = getString(R.string.my_collect)
        PageRefreshLayout.startIndex = 0
        mViewBind.let {
            it.rv.apply {
                this.adapter = adapter
                this.initFloatBtn(it.fab)
            }
        }
        onRefresh()
    }

    private fun onRefresh() {

        mViewBind.page.onRefresh {

            scope {
                collectData =
                    Get<ApiBaseResponse<ApiPagerResponse<CollectResponse>>>("/lg/collect/list/$index/json").await()
                if (first && collectData.data.datas.isEmpty()) {
                    showEmpty()
                } else {
                    first = false
                    index += if (index == 0) {
                        adapter.setList(collectData.data.datas)
                        1
                    } else {
                        if (collectData.data.datas.isEmpty()) {
                            //没有更多数据，结束动画，显示内容(没有更多数据)
                            showContent(false)
                            return@scope
                        }
                        //添加数据
                        adapter.addData(collectData.data.datas)
                        1
                    }
                }
                showContent(true)
            }

        }.autoRefresh()
    }
}