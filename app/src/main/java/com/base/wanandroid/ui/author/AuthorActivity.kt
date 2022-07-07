package com.base.wanandroid.ui.author

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import cn.nekocode.rxlifecycle.LifecycleEvent
import cn.nekocode.rxlifecycle.compact.RxLifecycleCompact
import com.base.wanandroid.application.WanAndroidApplication
import com.base.wanandroid.base.BaseActivity
import com.base.wanandroid.bean.ArticleListResponse
import com.base.wanandroid.bean.ArticleResponse
import com.base.wanandroid.bean.ShareListResponse
import com.base.wanandroid.bean.ShareResponse
import com.base.wanandroid.bean.base.ApiBaseResponse
import com.base.wanandroid.bean.base.ApiPagerResponse
import com.base.wanandroid.bean.base.ApiResponse
import com.base.wanandroid.constant.Constant.URI.SearchArticleById
import com.base.wanandroid.constant.Constant.URI.SearchArticleByName
import com.base.wanandroid.databinding.ActivityAuthorBinding
import com.base.wanandroid.ui.adapter.ArticleAdapter
import com.base.wanandroid.ui.collect.ArticleViewModel
import com.base.wanandroid.ui.home.ArticleDiffCallBack
import com.base.wanandroid.utils.BASE_URL
import com.base.wanandroid.utils.GenerateAvatarURL
import com.base.wanandroid.utils.RxTransformer
import com.base.wanandroid.utils.initFloatBtn
import com.base.wanandroid.utils.lifecycleOwner
import com.base.wanandroid.widget.layout.XCollapsingToolbarLayout
import com.drake.brv.PageRefreshLayout
import com.drake.net.Get
import com.drake.net.utils.scope
import com.drake.net.utils.scopeNetLife
import com.drake.serialize.intent.bundle
import java.io.File

/**
 * @author jiangshiyu
 * @date 2022/6/22
 * 作者页
 */
class AuthorActivity : BaseActivity<ActivityAuthorBinding, ArticleViewModel>() {

    private val name: String by bundle()
    private val userId: Int by bundle()

    private var first = false

    private val adapter: ArticleAdapter by lazy {
        ArticleAdapter(this, viewModel).apply {
            this.setDiffCallback(ArticleDiffCallBack())
        }
    }

    /** 按照作者昵称搜索文章 数据集 */
    private lateinit var dataByName: ApiBaseResponse<ApiPagerResponse<ArticleResponse>>

    /** 分享人对应列表数据 数据集 */
    private lateinit var dataByID: ApiBaseResponse<ShareResponse>

    override fun onBundle(bundle: Bundle) {


    }

    override fun init(savedInstanceState: Bundle?) {
        //标题栏返回按钮关闭页面
        binding?.titleBar?.leftView?.setOnClickListener { finishAfterTransition() }
        //是否作者(userid为-1)
        if (userId == -1) {
            PageRefreshLayout.startIndex = 0
        } else {
            PageRefreshLayout.startIndex = 1
        }
        binding?.headerText?.text = name
        binding?.let {
            it.rv.apply {
                this.adapter = adapter
                this.initFloatBtn(it.fab)
            }
        }

        //生成头像
        loadHeaderImage()
        initCollapsingToolbar()
        onRefresh()
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
            binding?.headerImage?.setImageBitmap(mBitmap)
        }
    }

    /** 折叠工具栏回调方法 */
    private fun initCollapsingToolbar() {
        binding?.collapsingToolbar?.setOnScrimsListener(object :
            XCollapsingToolbarLayout.OnScrimsListener {
            override fun onScrimsStateChange(layout: XCollapsingToolbarLayout?, shown: Boolean) {
                //工具栏折叠时设置标题，隐藏用户名，否则不设置标题，显示用户名
                if (shown) {
                    binding?.titleBar?.title = name
                    binding?.headerText?.visibility = View.INVISIBLE
                } else {
                    binding?.titleBar?.title = ""
                    binding?.headerText?.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun onRefresh() {
        binding?.page?.onRefresh {
            scope {
                if (userId == -1) {
                    dataByName =
                        Get<ApiBaseResponse<ApiPagerResponse<ArticleResponse>>>("/${SearchArticleByName}/$index/json?author=$name").await()
                    if (first && dataByName.data.datas.isEmpty()) {
                        showEmpty()
                    } else {
                        first = false
                        index += if (index == 0) {
                            adapter.setList(dataByName.data.datas)
                            1
                        } else {
                            if (dataByName.data.datas.isNullOrEmpty()) {
                                showContent(false)
                                return@scope
                            }
                            adapter.addData(dataByName.data.datas)
                            1
                        }
                    }
                    showContent(true)
                } else {
                    dataByID =
                        Get<ApiBaseResponse<ShareResponse>>("${BASE_URL}/${SearchArticleById}/$userId/share_articles/$index/json").await()
                    if (first && dataByID.data.shareArticles.datas.isEmpty()) {
                        showEmpty()
                    } else {
                        first = false
                        index += if (index == 0) {
                            adapter.setList(dataByID.data.shareArticles.datas)
                            1
                        } else {
                            if (dataByID.data.shareArticles.datas.isNullOrEmpty()) {
                                showContent(false)
                                return@scope
                            }
                            adapter.addData(dataByID.data.shareArticles.datas)
                            1
                        }
                    }
                    showContent(true)
                }
            }

        }?.autoRefresh()
    }
}