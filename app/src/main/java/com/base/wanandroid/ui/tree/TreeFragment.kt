package com.base.wanandroid.ui.tree

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import cn.nekocode.rxlifecycle.LifecycleEvent
import cn.nekocode.rxlifecycle.compact.RxLifecycleCompact
import com.base.wanandroid.base.BaseFragment
import com.base.wanandroid.base.BaseFragment1
import com.base.wanandroid.databinding.FragmentProjectChildBinding
import com.base.wanandroid.databinding.FragmentTreeBinding
import com.base.wanandroid.ext.init
import com.base.wanandroid.ext.loadServiceInit
import com.base.wanandroid.ext.showError
import com.base.wanandroid.ext.showLoading
import com.base.wanandroid.ui.adapter.TreeAdapter
import com.base.wanandroid.utils.RxTransformer
import com.base.wanandroid.utils.initFloatBtn
import com.base.wanandroid.utils.lifecycleOwner
import com.base.wanandroid.widget.recyclerview.SpaceItemDecoration
import com.blankj.utilcode.util.ConvertUtils
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir

/**
 * @author jiangshiyu
 * @date 2022/6/2
 */
class TreeFragment : BaseFragment1<TreeViewModel, FragmentProjectChildBinding>() {


    private val treeAdapter by lazy { TreeAdapter(arrayListOf()) }

    private lateinit var loadSir: LoadService<Any>


    override fun initView(savedInstanceState: Bundle?) {

        //状态配置
        loadSir = loadServiceInit(mViewBind.swipeRefresh) {
            loadSir.showLoading()
            mViewModel.getSystemData()
        }
        mViewBind.rvList.init(LinearLayoutManager(context), treeAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
            it.initFloatBtn(mViewBind.fab)
        }
        mViewBind.swipeRefresh.init {
            mViewModel.getSystemData()
        }


    }

    override fun lazyLoadData() {
        loadSir.showLoading()
        mViewModel.getSystemData()
    }


    override fun createObserver() {
        mViewModel.systemDataState.observe(viewLifecycleOwner) {
            mViewBind.swipeRefresh.isRefreshing = false
            if (it.isSuccess) {
                loadSir.showSuccess()
                treeAdapter.setList(it.listData)
            } else {
                loadSir.showError(it.errMessage)
            }
        }
    }
}