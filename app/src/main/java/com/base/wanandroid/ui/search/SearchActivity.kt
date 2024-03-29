package com.base.wanandroid.ui.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.lifecycleScope
import cn.nekocode.rxlifecycle.LifecycleEvent
import cn.nekocode.rxlifecycle.compact.RxLifecycleCompact
import com.base.wanandroid.R
import com.base.wanandroid.base.BaseActivity
import com.base.wanandroid.data.SearchResponse
import com.base.wanandroid.databinding.ActivitySearchBinding
import com.base.wanandroid.ui.adapter.SearchHistoryAdapter
import com.base.wanandroid.ui.adapter.SearchHotAdapter
import com.base.wanandroid.utils.AppConfig
import com.base.wanandroid.utils.RxTransformer
import com.base.wanandroid.viewmodel.request.RequestSearchViewModel
import com.drake.serialize.intent.openActivity
import com.google.android.flexbox.FlexboxLayoutManager
import com.rainy.monitor.utils.viewModels
import kotlinx.coroutines.launch
import me.hgj.jetpackmvvm.ext.parseState

/**
 * @author jiangshiyu
 * @date 2022/6/16
 */
class SearchActivity : BaseActivity<SearchViewModel, ActivitySearchBinding>() {


    private val hotAdapter: SearchHotAdapter by lazy { SearchHotAdapter() }

    private val historyAdapter: SearchHistoryAdapter by lazy { SearchHistoryAdapter() }

    private val requestSearchViewModel: RequestSearchViewModel by viewModels()


    override fun initView(savedInstanceState: Bundle?) {

        mViewBind.searchBar.back.setOnClickListener {
            finishAfterTransition()
        }

        mViewBind.clear.setOnClickListener {
            AppConfig.SearchHistory = arrayListOf()
            historyAdapter.setList(arrayListOf())
        }

        initSearchHot()
        initSearchHistory()

        //输入框监听软键盘操作
        mViewBind.searchBar.searchText.setOnEditorActionListener { _, actionId, _ ->
            //当点击软键盘的搜索键时搜索输入框中内容
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                //文本为空不执行操作
                if (mViewBind.searchBar.searchText.text.isNullOrBlank()) {
                    return@setOnEditorActionListener false
                } else {
                    openActivity<SearchResultActivity>(
                        "key" to mViewBind.searchBar.searchText.text.toString().trim()
                    )
                    updateKey(mViewBind.searchBar.searchText.text.toString().trim())
                }
            }
            return@setOnEditorActionListener false
        }
        //搜索图标跳转搜索结果Activity传递参数并更新搜索记录
        mViewBind.searchBar.search.setOnClickListener {
            if (mViewBind.searchBar.searchText.text.isNullOrBlank()) {
                return@setOnClickListener
            } else {
                openActivity<SearchResultActivity>(
                    "key" to mViewBind.searchBar.searchText.text.toString().trim()
                )
                updateKey(mViewBind.searchBar.searchText.text.toString().trim())
            }
        }
    }


    //初始化搜索热词
    private fun initSearchHot() {
        //初始化热门搜索RecyclerView
        mViewBind.rvHot.run {
            //使用伸缩布局
            layoutManager = FlexboxLayoutManager(context)
            //避免item改变重新绘制rv
            setHasFixedSize(true)
            //禁用嵌套滚动
            isNestedScrollingEnabled = false
            //设置adapter
            this.adapter = hotAdapter.run {
                //点击热词item标签跳转搜索结果Activity传递参数并更新搜索记录
                setOnItemClickListener { _, _, position ->
                    openActivity<SearchResultActivity>("key" to this.data[position].name)
                    updateKey(this.data[position].name)
                }
                //返回此adapter
                this
            }
        }
    }


    //初始化搜索历史
    @SuppressLint("NotifyDataSetChanged")
    private fun initSearchHistory() {
        if (AppConfig.SearchHistory.isNotEmpty()) {
            //搜索过则给adapter设置数据
            historyAdapter.setList(AppConfig.SearchHistory)
        }
        mViewBind.rvHistory.adapter = historyAdapter.run {
            //点击历史记录item跳转搜索结果Activity传递参数并更新搜索记录
            setOnItemClickListener { _, _, position ->
                openActivity<SearchResultActivity>("key" to this.data[position])
                updateKey(this.data[position])
            }
            //注册需要点击的子控件id
            addChildClickViewIds(R.id.item_history_image)
            setOnItemChildClickListener { _, view, position ->
                when (view.id) {
                    //点击子item删除图标
                    R.id.item_history_image -> {
                        //从adapter删除指定数据
                        historyAdapter.removeAt(position)
                        AppConfig.SearchHistory.apply {
                            //从存储中删除指定数据
                            removeAt(position)
                            //改变序列化对象内的字段要求重新赋值
                            AppConfig.SearchHistory = this
                        }
                    }
                }
            }
            //返回此adapter
            this
        }

    }

    //更新搜索记录
    private fun updateKey(keyStr: String) {
        AppConfig.SearchHistory.apply {
            if (contains(keyStr)) {
                //当搜索记录中包含该数据时 删除
                remove(keyStr)
                //同时从adapter中删除
                historyAdapter.remove(keyStr)
            } else if (size >= 10) {
                //如果集合的size 有10个以上了，删除最后一个
                removeAt(size - 1)
                //同时从adapter中删除
                historyAdapter.removeAt(size - 1)
            }
            //添加新数据到第一条
            add(0, keyStr)
            //同时添加到adapter
            historyAdapter.addData(0, keyStr)
            //滚动到rv顶部
            mViewBind.rvHistory.scrollToPosition(0)
            //改变序列化对象内的字段要求重新赋值
            AppConfig.SearchHistory = this
        }
    }

    override fun onResume() {
        super.onResume()
        requestSearchViewModel.getHotData()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun createObserver() {
        requestSearchViewModel.hotData.observe(this) { resultState ->
            parseState(resultState, {
                if (AppConfig.SearchHot.isEmpty()) {
                    AppConfig.SearchHot.addAll(it)
                    hotAdapter.setList(it)
                    hotAdapter.notifyDataSetChanged()
                }
            })
        }
    }
}