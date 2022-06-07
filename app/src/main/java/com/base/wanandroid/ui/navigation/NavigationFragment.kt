package com.base.wanandroid.ui.navigation

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.nekocode.rxlifecycle.LifecycleEvent
import cn.nekocode.rxlifecycle.compact.RxLifecycleCompact
import com.base.wanandroid.base.BaseFragment
import com.base.wanandroid.databinding.FragmentNavigationBinding
import com.base.wanandroid.ui.adapter.NavigationContentAdapter
import com.base.wanandroid.ui.adapter.NavigationTabAdapter
import com.base.wanandroid.utils.RxTransformer
import kotlinx.coroutines.launch
import q.rorbin.verticaltablayout.VerticalTabLayout
import q.rorbin.verticaltablayout.widget.TabView

/**
 * @author jiangshiyu
 * @date 2022/6/6
 */
class NavigationFragment : BaseFragment<FragmentNavigationBinding, NavigationViewModel>() {


    private var first = true

    /** 是否选中标签 */
    private var clickTab = false


    /** 当前位置索引 */
    private var currentIndex = 0

    /** 是否可以滚动 */
    private var scroll = false

    private val linearLayoutManager: LinearLayoutManager by lazy { LinearLayoutManager(activity) }
    private val navigationAdapter by lazy { NavigationContentAdapter() }

    override fun onBundle(bundle: Bundle) {

    }

    override fun init(savedInstanceState: Bundle?) {
        initAdapter()
        if (first) {
            lifecycleScope.launch {
                viewModel.getNavigationData()
                    .compose(
                        RxLifecycleCompact.bind(this@NavigationFragment)
                            .disposeObservableWhen(LifecycleEvent.DESTROY_VIEW)
                    )
                    .compose(RxTransformer.async())
                    .subscribe {
                        binding?.verticalTabLayout?.setTabAdapter(NavigationTabAdapter(it.data))
                        navigationAdapter.setList(it.data)
                        first = false
                        linkLeftRight()
                    }
            }
        }


    }

    private fun linkLeftRight() {
        binding?.rv?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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

        binding?.verticalTabLayout?.addOnTabSelectedListener(object :
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
        if (indexDistance > 0 && indexDistance < binding?.rv?.childCount ?: 0) {
            val top: Int = binding?.rv?.getChildAt(indexDistance)?.top ?: 0
            binding?.rv?.smoothScrollBy(0, top)
        }
    }

    override fun onPause() {
        binding?.rv?.stopScroll()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()

    }

    private fun initAdapter() {
        binding?.rv?.let {
            it.layoutManager = linearLayoutManager
            it.adapter = navigationAdapter
        }
    }

    /** 滚动右边rv，以选择左边标签 */
    private fun setChecked(position: Int) {
        if (clickTab) {
            clickTab = false
        } else {
            binding?.verticalTabLayout?.setTabSelected(currentIndex)
        }
        currentIndex = position
    }

    /** 选择左边标签，以滚动右边rv */
    private fun selectTab(position: Int) {
        currentIndex = position
        binding?.rv?.stopScroll()
        smoothScrollToPosition(position)
    }

    /** rv平滑滚动到指定位置 */
    private fun smoothScrollToPosition(position: Int) {
        val firstPosition: Int = linearLayoutManager.findFirstVisibleItemPosition()
        val lastPosition: Int = linearLayoutManager.findLastVisibleItemPosition()
        when {
            position <= firstPosition -> {
                binding?.rv?.smoothScrollToPosition(position)
            }
            position <= lastPosition -> {
                val top: Int? = binding?.rv?.getChildAt(position - firstPosition)?.top
                if (top != null) {
                    binding?.rv?.smoothScrollBy(0, top)
                }
            }
            else -> {
                binding?.rv?.smoothScrollToPosition(position)
                scroll = true
            }
        }
    }
}