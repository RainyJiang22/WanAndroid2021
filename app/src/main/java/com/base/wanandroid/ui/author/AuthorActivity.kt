package com.base.wanandroid.ui.author

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.wanandroid.application.WanAndroidApplication
import com.base.wanandroid.application.appViewModel
import com.base.wanandroid.application.eventViewModel
import com.base.wanandroid.base.BaseActivity
import com.base.wanandroid.databinding.ActivityAuthorBinding
import com.base.wanandroid.ext.init
import com.base.wanandroid.ext.initFooter
import com.base.wanandroid.ext.loadListData
import com.base.wanandroid.ext.loadServiceInit
import com.base.wanandroid.ext.showLoading
import com.base.wanandroid.ui.adapter.ArticleNewAdapter
import com.base.wanandroid.ui.collect.ArticleViewModel
import com.base.wanandroid.ui.collect.CollectBus
import com.base.wanandroid.ui.home.ArticleDiffNewCallBack
import com.base.wanandroid.utils.GenerateAvatarURL
import com.base.wanandroid.utils.initFloatBtn
import com.base.wanandroid.viewmodel.request.RequestCollectViewModel
import com.base.wanandroid.widget.layout.XCollapsingToolbarLayout
import com.base.wanandroid.widget.recyclerview.SpaceItemDecoration
import com.blankj.utilcode.util.ConvertUtils
import com.drake.net.Get
import com.drake.net.utils.scopeNetLife
import com.drake.serialize.intent.bundle
import com.kingja.loadsir.core.LoadService
import com.rainy.monitor.utils.viewModels
import java.io.File

/**
 * @author jiangshiyu
 * @date 2022/6/22
 * 作者页
 */
class AuthorActivity : BaseActivity<ArticleViewModel, ActivityAuthorBinding>() {

    private val name: String by bundle()
    private val userId: Int by bundle()

    //界面状态管理者
    private lateinit var loadSir: LoadService<Any>

    private val requestCollectViewModel: RequestCollectViewModel by viewModels()


    private val adapter: ArticleNewAdapter by lazy {
        ArticleNewAdapter(this).apply {
            this.setDiffCallback(ArticleDiffNewCallBack())
        }
    }


    override fun initView(savedInstanceState: Bundle?) {
        //标题栏返回按钮关闭页面
        mViewBind.titleBar.leftView?.setOnClickListener { finishAfterTransition() }
        mViewBind.headerText.text = name

        //生成头像
        loadHeaderImage()
        initCollapsingToolbar()

        loadSir = loadServiceInit(mViewBind.swipeRefresh) {
            loadSir.showLoading()
            mViewModel.getLookInfo(userId, true)
        }

        mViewBind.rvHomeList.init(LinearLayoutManager(this), adapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
            it.initFooter {
                mViewModel.getLookInfo(userId, false)
            }
            it.initFloatBtn(mViewBind.fab)
        }

        mViewBind.swipeRefresh.init {
            mViewModel.getLookInfo(userId, false)
        }

        adapter.run {
            setCollectClick { item, v, position ->
                if (v.isChecked) {
                    requestCollectViewModel.unCollect(item.id)
                } else {
                    requestCollectViewModel.collect(item.id)
                }
            }
        }
    }

    private fun loadHeaderImage() {
        scopeNetLife {
            val mAvatar =
                Get<File>("${GenerateAvatarURL}/$name.png?apikey=${WanAndroidApplication.apiKey}") {
                    setDownloadDir(WanAndroidApplication.getApplication()!!.filesDir)
                    setDownloadMd5Verify()
                    setDownloadFileNameDecode()
                    setDownloadTempFile()
                }.await()
            //文件解码bitmap
            val mBitmap = BitmapFactory.decodeFile(mAvatar.path)
            //设置头像
            mViewBind.headerImage.setImageBitmap(mBitmap)
        }
    }

    /** 折叠工具栏回调方法 */
    private fun initCollapsingToolbar() {
        mViewBind.collapsingToolbar.setOnScrimsListener(object :
            XCollapsingToolbarLayout.OnScrimsListener {
            override fun onScrimsStateChange(layout: XCollapsingToolbarLayout?, shown: Boolean) {
                //工具栏折叠时设置标题，隐藏用户名，否则不设置标题，显示用户名
                if (shown) {
                    mViewBind.titleBar.title = name
                    mViewBind.headerText.visibility = View.INVISIBLE
                } else {
                    mViewBind.titleBar.title = ""
                    mViewBind.headerText.visibility = View.VISIBLE
                }
            }
        })
    }


    override fun onResume() {
        super.onResume()
        loadSir.showLoading()
        mViewModel.getLookInfo(userId, true)
    }

    override fun createObserver() {

        mViewModel.shareListDataUiState.observe(this) {
            loadListData(it, adapter, loadSir, mViewBind.rvHomeList, mViewBind.swipeRefresh)
        }

        requestCollectViewModel.collectUiState.observe(this) {
            if (it.isSuccess) {
                //收藏或取消收藏操作成功，发送全局收藏消息
                eventViewModel.collectEvent.value = CollectBus(it.id, it.collect)
            } else {
                for (index in adapter.data.indices) {
                    if (adapter.data[index].id == it.id) {
                        adapter.data[index].collect = it.collect
                        adapter.notifyItemChanged(index)
                        break
                    }
                }
            }
        }
        appViewModel.run {
            //监听账户信息是否改变 有值时(登录)将相关的数据设置为已收藏，为空时(退出登录)，将已收藏的数据变为未收藏
            userInfo.observeInActivity(this@AuthorActivity) {
                if (it != null) {
                    it.collectIds.forEach { id ->
                        for (item in adapter.data) {
                            if (id.toInt() == item.id) {
                                item.collect = true
                                break
                            }
                        }
                    }
                } else {
                    for (item in adapter.data) {
                        item.collect = false
                    }
                }
                adapter.notifyDataSetChanged()
            }
            //监听全局的收藏信息 收藏的Id跟本列表的数据id匹配则需要更新
            eventViewModel.collectEvent.observeInActivity(this@AuthorActivity) {
                for (index in adapter.data.indices) {
                    if (adapter.data[index].id == it.id) {
                        adapter.data[index].collect = it.collect
                        adapter.notifyItemChanged(index)
                        break
                    }
                }
            }
        }
    }
}