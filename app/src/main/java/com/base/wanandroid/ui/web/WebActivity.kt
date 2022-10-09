package com.base.wanandroid.ui.web

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.base.wanandroid.R
import com.base.wanandroid.application.eventViewModel
import com.base.wanandroid.base.BaseActivity
import com.base.wanandroid.data.CollectResponse
import com.base.wanandroid.databinding.ActivityWebBinding
import com.base.wanandroid.history.HistoryRepository
import com.base.wanandroid.history.bean.HistoryEntity
import com.base.wanandroid.utils.getAgentWeb
import com.base.wanandroid.utils.html2Spanned
import com.base.wanandroid.utils.html2String
import com.google.android.material.appbar.AppBarLayout
import com.just.agentweb.AgentWeb
import com.just.agentweb.NestedScrollAgentWebView
import com.just.agentweb.WebChromeClient
import com.base.wanandroid.ui.collect.ArticleViewModel
import com.base.wanandroid.ui.collect.CollectBus
import com.base.wanandroid.utils.AppConfig
import com.base.wanandroid.utils.RxTransformer
import com.base.wanandroid.viewmodel.request.RequestCollectViewModel
import com.blankj.utilcode.util.ToastUtils
import com.drake.channel.sendEvent
import com.drake.channel.sendTag
import com.drake.serialize.intent.browse
import com.drake.serialize.intent.share
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author jiangshiyu
 * @date 2022/6/2
 */
class WebActivity : BaseActivity<ArticleViewModel, ActivityWebBinding>() {

    private lateinit var mAgentWeb: AgentWeb
    private lateinit var shareTitle: String
    private lateinit var shareUrl: String
    private var shareId = -1
    private var isCollect = false
    private var isArticle = true
    private var originId = -1

    private var historySource: HistoryEntity? = null
    private var data: CollectResponse? = null

    private val requestCollectViewModel: RequestCollectViewModel by viewModels()

    companion object {

        /** id key */
        const val CONTENT_ID_KEY = "id"

        /** originId key */
        const val CONTENT_ORIGIN_ID_KEY = "originId"

        /** title key */
        const val CONTENT_TITLE_KEY = "title"

        /** url key */
        const val CONTENT_URL_KEY = "url"

        const val CONTENT_COLLECT_KEY = "collect"

        /** article key */
        const val CONTENT_ARTICLE_KEY = "article"

        //收藏内容
        const val CONTENT_DATA_KEY = "data"

        /**
         * 从文章打开网页
         *
         * @param context 上下文对象
         * @param id 文章ID
         * @param title 文章标题
         * @param url 文章URL
         * @param article 是否文章(不用填)
         * @param originId 文章原始ID(收藏页面跳转时使用，网页取消收藏时同时从收藏列表中删除)
         * @param data 收藏数据源
         */
        fun start(
            context: Context,
            id: Int,
            title: String,
            url: String,
            collect: Boolean = false,
            article: Boolean = true,
            originId: Int = -1,
            data: CollectResponse? = null
        ) {
            Intent(context, WebActivity::class.java).run {
                putExtra(CONTENT_ID_KEY, id)
                putExtra(CONTENT_ORIGIN_ID_KEY, originId)
                putExtra(CONTENT_TITLE_KEY, title)
                putExtra(CONTENT_URL_KEY, url)
                putExtra(CONTENT_COLLECT_KEY, collect)
                putExtra(CONTENT_ARTICLE_KEY, article)
                putExtra(CONTENT_DATA_KEY, data)
                context.startActivity(this)
            }
        }

        /**
         * 普通打开网页
         *
         * @param context 上下文对象
         * @param url 网址URL
         */
        fun start(context: Context, url: String) {
            //不是从文章进来
            start(context, -1, "", url, article = false)
        }

    }

    override fun initView(savedInstanceState: Bundle?) {
        intent?.extras?.let {
            shareId = it.getInt(CONTENT_ID_KEY, -1)
            originId = it.getInt(CONTENT_ORIGIN_ID_KEY, -1)
            shareTitle = it.getString(CONTENT_TITLE_KEY, "")
            isCollect = it.getBoolean(CONTENT_COLLECT_KEY, false)
            shareUrl = it.getString(CONTENT_URL_KEY, "")
            isArticle = it.getBoolean(CONTENT_ARTICLE_KEY, true)
            data = it.getParcelable(CONTENT_DATA_KEY)
        }

        mViewBind.toolbar.apply {
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { finishAfterTransition() }
            navigationContentDescription = ""
        }

        mViewBind.title.apply {
            text = getString(R.string.loading)
            visibility = View.VISIBLE
            postDelayed({
                this.isSelected = true
            }, 2000)
        }
        initWebView()
    }


    private val mWebChromeClient = object : WebChromeClient() {
        @RequiresApi(Build.VERSION_CODES.N)
        override fun onReceivedTitle(view: WebView, title: String) {
            super.onReceivedTitle(view, title)
            //只对不是以URL链接为标题的网页执行以下操作(e.g.微信公众号会先显示网页链接，再显示标题)
            if (title.isNotEmpty() && !title.startsWith("http")) {
                //设置网页标题
                this@WebActivity.mViewBind.title.text = title.html2Spanned()
                //设置网页分享标题
                this@WebActivity.shareTitle = title.html2String()
                //设置网页分享URL
                this@WebActivity.shareUrl = view.url.toString()

                val historyRecordBean = historySource?.copy(
                    date = getNowDateTime()
                ) ?: HistoryEntity(
                    null,
                    shareTitle,
                    shareUrl,
                    getNowDateTime()
                )
                HistoryRepository
                    .saveHistoryRecord(historyRecordBean)
                    .subscribeOn(Schedulers.io())
                    .subscribe()
            }
        }
    }

    //获取当前完整时间
    @SuppressLint("SimpleDateFormat")
    fun getNowDateTime(): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return simpleDateFormat.format(Date())
    }


    private fun initWebView() {
        val webView = NestedScrollAgentWebView(this)
        val layoutParams = CoordinatorLayout.LayoutParams(-1, -1)
        layoutParams.behavior = AppBarLayout.ScrollingViewBehavior()

        mAgentWeb = shareUrl.getAgentWeb(
            this,
            mViewBind.webContainer,
            layoutParams,
            webView,
            BaseWebClient(),
            mWebChromeClient
        )
        mAgentWeb.webCreator.webView.apply {
            overScrollMode = WebView.OVER_SCROLL_NEVER
            settings.useWideViewPort = true
            settings.loadWithOverviewMode = true
            settings.allowFileAccess = false
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_web, menu)
        if (!isArticle) {
            //隐藏收藏
            menu?.findItem(R.id.web_collect)?.isVisible = false
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        //如果收藏了，右上角的收藏图标相对应改变
        this.let {
            if (isCollect) {
                //已收藏
                menu?.findItem(R.id.web_collect)?.icon =
                    ContextCompat.getDrawable(it, R.drawable.ic_collect_strawberry)
            } else {
                //未收藏
                menu?.findItem(R.id.web_collect)?.icon =
                    ContextCompat.getDrawable(it, R.drawable.ic_collect_black_24)
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.web_collect -> {
                if (AppConfig.UserName.isEmpty()) {
                    ToastUtils.showShort(getString(R.string.please_login))
                    return true
                }
                isCollect = !isCollect

                if (isCollect) {
                    val indexId = if (originId == -1) {
                        shareId
                    } else {
                        originId
                    }
                    //收藏
                    item.setIcon(R.drawable.ic_collect_strawberry)
                    requestCollectViewModel.collect(indexId)
                } else {
                    //取消收藏
                    item.setIcon(R.drawable.ic_collect_black_24)
                    requestCollectViewModel.unCollect(originId)
                }
            }

            //分享
            R.id.web_share -> {
                share(
                    getString(
                        R.string.web_share_url,
                        getString(R.string.app_name),
                        shareTitle,
                        shareUrl
                    ),
                    getString(R.string.text_plan),
                    getString(R.string.web_share)
                )
            }

            R.id.web_browser -> {
                //用网页打开
                browse(shareUrl)
            }
            R.id.web_refresh -> {
                //刷新
                mAgentWeb.urlLoader?.reload()
            }
        }

        return super.onOptionsItemSelected(item)
    }


    override fun onBackPressed() {
        mAgentWeb.run {
            if (!back()) {
                super.onBackPressed()
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onResume() {
        mAgentWeb.webLifeCycle.onResume()
        requestCollectViewModel.collectUiState.observe(this) {
            if (it.isSuccess) {
                eventViewModel.collectEvent.value = CollectBus(it.id, it.collect)
                //刷新一下menu
                this.window?.invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL)
                this.invalidateOptionsMenu()
            } else {
                ToastUtils.showShort(it.errorMsg)
            }
        }

        super.onResume()
    }

    override fun onPause() {
        mAgentWeb.webLifeCycle.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        if (originId != -1 && !isCollect) {
            //从收藏列表进来，并且最后取消收藏，则发送事件同步收藏列表(删除这条item)
            data?.let { sendEvent(it) }
        } else {
            //如果收藏，发送标签(true)
            if (isCollect) {
                sendTag(true.toString())
            } else {
                //如果未收藏发送标签(false)
                sendTag(false.toString())
            }
        }
        mAgentWeb.webLifeCycle.onDestroy()
        setSupportActionBar(null)
        super.onDestroy()
    }

}