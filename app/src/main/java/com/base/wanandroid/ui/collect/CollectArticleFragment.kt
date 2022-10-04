package com.base.wanandroid.ui.collect

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.wanandroid.application.eventViewModel
import com.base.wanandroid.base.BaseFragment
import com.base.wanandroid.databinding.FragmentChildListBinding
import com.base.wanandroid.ext.init
import com.base.wanandroid.ext.initFooter
import com.base.wanandroid.ext.loadListData
import com.base.wanandroid.ext.loadServiceInit
import com.base.wanandroid.ext.showEmpty
import com.base.wanandroid.ext.showLoading
import com.base.wanandroid.ui.adapter.CollectAdapter
import com.base.wanandroid.widget.recyclerview.SpaceItemDecoration
import com.blankj.utilcode.util.ConvertUtils
import com.kingja.loadsir.core.LoadService

/**
 * @author jiangshiyu
 * @date 2022/10/4
 * 收藏文章列表
 */
class CollectArticleFragment : BaseFragment<CollectViewModel, FragmentChildListBinding>() {


    //界面状态管理
    private lateinit var loadSir: LoadService<Any>

    private val collectAdapter by lazy { CollectAdapter(arrayListOf()) }


    override fun initView(savedInstanceState: Bundle?) {

        loadSir = loadServiceInit(mViewBind.swipeRefresh) {
            loadSir.showLoading()
            mViewModel.getCollectArticleData(true)
        }

        mViewBind.rvList.init(LinearLayoutManager(context), collectAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
            it.initFooter {
                mViewModel.getCollectArticleData(false)
            }
        }
        mViewBind.swipeRefresh.init {
            mViewModel.getCollectArticleData(true)
        }

        collectAdapter.run {
            setCollectClick { item, v, position ->
                if (v.isChecked) {
                    mViewModel.collect(item.originId)
                } else {
                    mViewModel.unCollect(item.originId)
                }
            }
        }
    }

    override fun lazyLoadData() {
        loadSir.showLoading()
        mViewModel.getCollectArticleData(true)
    }


    override fun createObserver() {
        mViewModel.articleDataState.observe(viewLifecycleOwner) {
            loadListData(it, collectAdapter, loadSir, mViewBind.rvList, mViewBind.swipeRefresh)
        }

        mViewModel.collectUiState.observe(viewLifecycleOwner) {
            if (it.isSuccess) {
                //收藏或者取消收藏操作成功，发送全局收藏消息
                eventViewModel.collectEvent.value = CollectBus(it.id, it.collect)
            } else {
                for (index in collectAdapter.data.indices) {
                    if (collectAdapter.data[index].originId == it.id) {
                        collectAdapter.notifyItemChanged(index)
                        break
                    }
                }
            }
        }

        eventViewModel.run {

            //全局监听收藏消息
            collectEvent.observeInFragment(this@CollectArticleFragment, Observer {
                for (index in collectAdapter.data.indices) {
                    if (collectAdapter.data[index].originId == it.id) {
                        collectAdapter.removeAt(index)
                        if (collectAdapter.data.isEmpty()) {
                            loadSir.showEmpty()
                        }
                        return@Observer
                    }
                }
                mViewModel.getCollectArticleData(true)
            })
        }
    }
}