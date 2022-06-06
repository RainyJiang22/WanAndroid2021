package com.base.wanandroid.ui.web

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.annotation.RequiresApi
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.base.wanandroid.R
import com.base.wanandroid.base.BaseActivity
import com.base.wanandroid.databinding.ActivityWebBinding
import com.base.wanandroid.utils.getAgentWeb
import com.base.wanandroid.utils.html2Spanned
import com.base.wanandroid.utils.html2String
import com.google.android.material.appbar.AppBarLayout
import com.just.agentweb.AgentWeb
import com.just.agentweb.NestedScrollAgentWebView
import com.just.agentweb.WebChromeClient
import com.photoroom.editor.base.EmptyViewModel

/**
 * @author jiangshiyu
 * @date 2022/6/2
 */
class WebActivity : BaseActivity<ActivityWebBinding, EmptyViewModel>() {

    private lateinit var mAgentWeb: AgentWeb
    private lateinit var shareTitle: String
    private lateinit var shareUrl: String
    private var shareId = -1
    private var isArticle = true
    private var originId = -1

    companion object {

        /** id key */
        const val CONTENT_ID_KEY = "id"

        /** originId key */
        const val CONTENT_ORIGIN_ID_KEY = "originId"

        /** title key */
        const val CONTENT_TITLE_KEY = "title"

        /** url key */
        const val CONTENT_URL_KEY = "url"

        /** article key */
        const val CONTENT_ARTICLE_KEY = "article"

        /**
         * 从文章打开网页
         *
         * @param context 上下文对象
         * @param id 文章ID
         * @param title 文章标题
         * @param url 文章URL
         * @param article 是否文章(不用填)
         * @param originId 文章原始ID(收藏页面跳转时使用，网页取消收藏时同时从收藏列表中删除)
         */
        fun start(
            context: Context,
            id: Int,
            title: String,
            url: String,
            article: Boolean = true,
            originId: Int = -1
        ) {
            Intent(context, WebActivity::class.java).run {
                putExtra(CONTENT_ID_KEY, id)
                putExtra(CONTENT_ORIGIN_ID_KEY, originId)
                putExtra(CONTENT_TITLE_KEY, title)
                putExtra(CONTENT_URL_KEY, url)
                putExtra(CONTENT_ARTICLE_KEY, article)
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


    override fun onBundle(bundle: Bundle) {
    }

    override fun init(savedInstanceState: Bundle?) {
        intent?.extras?.let {
            shareId = it.getInt(CONTENT_ID_KEY, -1)
            originId = it.getInt(CONTENT_ORIGIN_ID_KEY, -1)
            shareTitle = it.getString(CONTENT_TITLE_KEY, "")
            shareUrl = it.getString(CONTENT_URL_KEY, "")
            isArticle = it.getBoolean(CONTENT_ARTICLE_KEY, true)
        }

        binding?.toolbar?.apply {
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { finishAfterTransition() }
            navigationContentDescription = ""
        }

        binding?.title?.apply {
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
                this@WebActivity.binding?.title?.text = title.html2Spanned()
                //设置网页分享标题
                this@WebActivity.shareTitle = title.html2String()
                //设置网页分享URL
                this@WebActivity.shareUrl = view.url.toString()
            }
        }
    }

    private fun initWebView() {
        val webView = NestedScrollAgentWebView(this)
        val layoutParams = CoordinatorLayout.LayoutParams(-1, -1)
        layoutParams.behavior = AppBarLayout.ScrollingViewBehavior()

        mAgentWeb = shareUrl.getAgentWeb(
            this,
            binding!!.webContainer,
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
}