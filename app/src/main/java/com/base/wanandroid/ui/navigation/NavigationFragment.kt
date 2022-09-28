package com.base.wanandroid.ui.navigation

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.base.wanandroid.base.BaseFragment1
import com.base.wanandroid.databinding.FragmentNavigationBinding
import com.base.wanandroid.ext.init
import com.base.wanandroid.ext.loadServiceInit
import com.base.wanandroid.ext.showError
import com.base.wanandroid.ext.showLoading
import com.base.wanandroid.ui.adapter.NavigationContentAdapter
import com.base.wanandroid.ui.adapter.NavigationTabAdapter
import com.kingja.loadsir.core.LoadService
import q.rorbin.verticaltablayout.VerticalTabLayout
import q.rorbin.verticaltablayout.widget.TabView

/**
 * @author jiangshiyu
 * @date 2022/6/6
 */
class NavigationFragment : BaseFragment1<NavigationViewModel, FragmentNavigationBinding>() {


    /** 是否选中标签 */
    private var clickTab = false

    /** 当前位置索引 */
    private var currentIndex = 0

    /** 是否可以滚动 */
    private var scroll = false

    private val linearLayoutManager: LinearLayoutManager by lazy { LinearLayoutManager(activity) }

    private val navigationAdapter by lazy { NavigationContentAdapter() }


    private lateinit var loadSir: LoadService<Any>


    override fun initView(savedInstanceState: Bundle?) {
        loadSir = loadServiceInit(mViewBind.swipeRefresh) {
            loadSir.showLoading()
            mViewModel.getNavigationData()
        }

        mViewBind.rv.init(linearLayoutManager, navigationAdapter)

        mViewBind.swipeRefresh.init {
            mViewModel.getNavigationData()
        }

    }

    override fun lazyLoadData() {
        loadSir.showLoading()
        mViewModel.getNavigationData()
    }

    override fun createObserver() {
        mViewModel.navigationDataState.observe(viewLifecycleOwner) {
            mViewBind.swipeRefresh.isRefreshing = false
            if (it.isSuccess) {
                loadSir.showSuccess()
                mViewBind.verticalTabLayout.setTabAdapter(NavigationTabAdapter(it.listData))
                navigationAdapter.setList(it.listData)
                linkLeftRight()
            } else {
                loadSir.showError(it.errMessage)
            }
        }
    }

    private fun linkLeftRight() {
        mViewBind.rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (scroll && (newState == RecyclerView.SCROLL_STATE_IDLE)) {
                    scrollRecyclerView()
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (scroll) {
                    scrollRecyclerView()
                }
            }
        })

        mViewBind.verticalTabLayout.addOnTabSelectedListener(object :
            VerticalTabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabView?, position: Int) {}

            override fun onTabSelected(tab: TabView?, position: Int) {
                clickTab = true
                selectTab(position)
            }
        })
    }

    private fun scrollRecyclerView() {
        scroll = false
        val indexDistance: Int = currentIndex - linearLayoutManager.findFirstVisibleItemPosition()
        if (indexDistance > 0 && indexDistance < mViewBind.rv.childCount) {
            val top: Int = mViewBind.rv.getChildAt(indexDistance)?.top ?: 0
            mViewBind.rv.smoothScrollBy(0, top)
        }
    }

    override fun onPause() {
        mViewBind.rv.stopScroll()
        super.onPause()
    }


    /** 滚动右边rv，以选择左边标签 */
    private fun setChecked(position: Int) {
        if (clickTab) {
            clickTab = false
        } else {
            mViewBind.verticalTabLayout.setTabSelected(currentIndex)
        }
        currentIndex = position
    }

    /** 选择左边标签，以滚动右边rv */
    private fun selectTab(position: Int) {
        currentIndex = position
        mViewBind.rv.stopScroll()
        smoothScrollToPosition(position)
    }

    /** rv平滑滚动到指定位置 */
    private fun smoothScrollToPosition(position: Int) {
        val firstPosition: Int = linearLayoutManager.findFirstVisibleItemPosition()
        val lastPosition: Int = linearLayoutManager.findLastVisibleItemPosition()
        when {
            position <= firstPosition -> {
                mViewBind.rv.smoothScrollToPosition(position)
            }
            position <= lastPosition -> {
                val top: Int? = mViewBind.rv.getChildAt(position - firstPosition)?.top
                if (top != null) {
                    mViewBind.rv.smoothScrollBy(0, top)
                }
            }
            else -> {
                mViewBind.rv.smoothScrollToPosition(position)
                scroll = true
            }
        }
    }
}