package com.base.wanandroid.ui.share

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.wanandroid.R
import com.base.wanandroid.base.BaseFragment
import com.base.wanandroid.data.ArticleResponse
import com.base.wanandroid.databinding.ActivityShareBinding
import com.base.wanandroid.ext.init
import com.base.wanandroid.ext.initFooter
import com.base.wanandroid.ext.loadListData
import com.base.wanandroid.ext.loadServiceInit
import com.base.wanandroid.ext.showEmpty
import com.base.wanandroid.ext.showLoading
import com.base.wanandroid.ui.adapter.ShareAdapter
import com.base.wanandroid.ui.web.WebActivity
import com.base.wanandroid.utils.initFloatBtn
import com.base.wanandroid.widget.Dialog
import com.base.wanandroid.widget.layout.SwipeItemLayout
import com.base.wanandroid.widget.recyclerview.SpaceItemDecoration
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ToastUtils
import com.drake.serialize.intent.openActivity
import com.kingja.loadsir.core.LoadService

/**
 * @author jiangshiyu
 * @date 2022/6/16
 * 我的分享
 */
class ShareFragment : BaseFragment<ShareViewModel, ActivityShareBinding>() {


    private val adapter by lazy { ShareAdapter(this, mViewModel) }

    private lateinit var loadSir: LoadService<Any>

    var onShareListener: OnShareListener? = null


    override fun initView(savedInstanceState: Bundle?) {

        mViewBind.titleBar.let {
            it.leftView.setOnClickListener {
                onShareListener?.removeFragment()
            }
            it.rightView.setOnClickListener {
                openActivity<ShareArticleActivity>()
            }
        }
        mViewBind.titleBar.leftView?.setOnClickListener {
            onShareListener?.removeFragment()
        }
        //标题栏右侧图标打开分享文章页面，获取返回结果，增加一条数据
        mViewBind.titleBar.rightView?.setOnClickListener {
            openActivity<ShareArticleActivity>()
        }
        loadSir = loadServiceInit(mViewBind.swipeRefresh) {
            loadSir.showLoading()
            mViewModel.getShareData(true)
        }


        mViewBind.rvList.init(LinearLayoutManager(context), adapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
            it.initFooter {
                //触发加载更多时请求数据
                mViewModel.getShareData(false)
            }
            //设置RecyclerView的侧滑监听器
            it.addOnItemTouchListener(SwipeItemLayout.OnSwipeItemTouchListener(requireContext()))
            //初始化FloatingActionButton
            it.initFloatBtn(mViewBind.fab)
        }

        mViewBind.swipeRefresh.init {
            mViewModel.getShareData(true)
        }

        adapter.run {
            setOnItemChildClickListener { adapter, view, position ->
                //获取到对应的item数据
                val item = adapter.data[position] as ArticleResponse
                when (view.id) {
                    R.id.share_item -> {
                        //跳转文章网页打开链接，传递文章id标题链接及收藏与否
                        item.run { WebActivity.start(context, id, title, link, collect) }
                    }
                    R.id.share_delete -> {
                        //删除对应分享文章
                        Dialog.getConfirmDialog(
                            context,
                            context.getString(R.string.delete_share_confirm)
                        ) { _, _ ->
                            //删除分享文章
                            mViewModel.deleteShareData(item.id, position)
                            //adapter中删除
                            adapter.removeAt(position)
                            ToastUtils.showShort(context.getString(R.string.delete_succeed))
                        }.show()
                    }
                }
            }
        }

    }

    override fun lazyLoadData() {
        loadSir.showLoading()
        mViewModel.getShareData(true)
    }


    override fun createObserver() {

        mViewModel.shareDataState.observe(viewLifecycleOwner) {
            loadListData(it, adapter, loadSir, mViewBind.rvList, mViewBind.swipeRefresh)
        }

        mViewModel.delDataState.observe(viewLifecycleOwner) {
            if (it.isSuccess) {
                if (adapter.data.size == 1) {
                    loadSir.showEmpty()
                }
                it.data?.let { data -> adapter.removeAt(data) }

            } else {
                //删除失败
                ToastUtils.showShort(it.errorMsg)
            }
        }
    }


}

interface OnShareListener {
    fun removeFragment()
}