package com.base.wanandroid.ui.collect

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.wanandroid.application.eventViewModel
import com.base.wanandroid.base.BaseFragment
import com.base.wanandroid.databinding.FragmentChildListBinding
import com.base.wanandroid.ext.init
import com.base.wanandroid.ext.initFooter
import com.base.wanandroid.ext.loadServiceInit
import com.base.wanandroid.ext.showEmpty
import com.base.wanandroid.ext.showError
import com.base.wanandroid.ext.showLoading
import com.base.wanandroid.ui.adapter.CollectAdapter
import com.base.wanandroid.ui.adapter.CollectUrlAdapter
import com.base.wanandroid.widget.recyclerview.SpaceItemDecoration
import com.blankj.utilcode.util.ConvertUtils
import com.kingja.loadsir.core.LoadService

/**
 * @author jiangshiyu
 * @date 2022/10/4
 * 收藏文章网址集合
 */
class CollectUrlFragment : BaseFragment<CollectViewModel, FragmentChildListBinding>() {

    //界面状态管理者
    private lateinit var loadSir: LoadService<Any>

    private val collectAdapter by lazy { CollectUrlAdapter(arrayListOf()) }


    override fun initView(savedInstanceState: Bundle?) {
        loadSir = loadServiceInit(mViewBind.swipeRefresh) {
            loadSir.showLoading()
            mViewModel.getCollectUrlData()
        }

        mViewBind.rvList.init(LinearLayoutManager(context), collectAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
            it.initFooter {
                mViewModel.getCollectUrlData()
            }
        }

        mViewBind.swipeRefresh.init {
            mViewModel.getCollectUrlData()
        }

        collectAdapter.run {
            setCollectClick { item, v, position ->
                if (v.isChecked) {
                    mViewModel.collectUrl(item.name, item.link)
                } else {
                    mViewModel.unCollectUrl(item.id)
                }
            }
        }


    }

    override fun lazyLoadData() {
        loadSir.showLoading()
        mViewModel.getCollectUrlData()
    }

    override fun createObserver() {
        mViewModel.urlDataState.observe(viewLifecycleOwner) {
            mViewBind.swipeRefresh.isRefreshing = false
            mViewBind.rvList.loadMoreFinish(it.isEmpty, it.hasMore)

            if (it.isSuccess) {
                //成功
                when {
                    //第一页并没有数据，显示空布局
                    it.isEmpty -> {
                        loadSir.showEmpty()
                    }

                    else -> {
                        loadSir.showSuccess()
                        collectAdapter.setList(it.listData)
                    }
                }
            } else {
                //失败
                loadSir.showError(it.errMessage)
            }
        }

        mViewModel.collectUrlUiState.observe(viewLifecycleOwner, Observer {
            if (it.isSuccess) {
                for (index in collectAdapter.data.indices) {
                    if (collectAdapter.data[index].id == it.id) {
                        collectAdapter.removeAt(index)
                        if (collectAdapter.data.size == 0) {
                            loadSir.showEmpty()
                        }
                        return@Observer
                    }
                }
            } else {
                for (index in collectAdapter.data.indices) {
                    if (collectAdapter.data[index].id == it.id) {
                        collectAdapter.notifyItemChanged(index)
                        break
                    }
                }
            }
        })
        eventViewModel.run {
            //监听全局的收藏信息 收藏的Id跟本列表的数据id匹配则 需要删除他 否则则请求最新收藏数据
            collectEvent.observeInFragment(this@CollectUrlFragment, Observer {
                for (index in collectAdapter.data.indices) {
                    if (collectAdapter.data[index].id == it.id) {
                        collectAdapter.data.removeAt(index)
                        collectAdapter.notifyItemChanged(index)
                        if (collectAdapter.data.size == 0) {
                            loadSir.showEmpty()
                        }
                        return@Observer
                    }
                }
                mViewModel.getCollectUrlData()
            })
        }

    }


}