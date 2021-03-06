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
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.annotation.RequiresApi
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewModelScope
import com.base.wanandroid.R
import com.base.wanandroid.base.BaseActivity
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
import com.base.wanandroid.base.EmptyViewModel
import com.base.wanandroid.bean.CollectResponse
import com.base.wanandroid.ui.collect.ArticleViewModel
import com.base.wanandroid.utils.AppConfig
import com.base.wanandroid.utils.RxTransformer
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
class WebActivity : BaseActivity<ActivityWebBinding, ArticleViewModel>() {

    private lateinit var mAgentWeb: AgentWeb
    private lateinit var shareTitle: String
    private lateinit var shareUrl: String
    private var shareId = -1
    private var isCollect = false
    private var isArticle = true
    private var originId = -1

    private var historySource: HistoryEntity? = null
    private var data: CollectResponse? = null

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

        //????????????
        const val CONTENT_DATA_KEY = "data"

        /**
         * ?????????????????????
         *
         * @param context ???????????????
         * @param id ??????ID
         * @param title ????????????
         * @param url ??????URL
         * @param article ????????????(?????????)
         * @param originId ????????????ID(?????????????????????????????????????????????????????????????????????????????????)
         * @param data ???????????????
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
         * ??????????????????
         *
         * @param context ???????????????
         * @param url ??????URL
         */
        fun start(context: Context, url: String) {
            //?????????????????????
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
            isCollect = it.getBoolean(CONTENT_COLLECT_KEY, false)
            shareUrl = it.getString(CONTENT_URL_KEY, "")
            isArticle = it.getBoolean(CONTENT_ARTICLE_KEY, true)
            data = it.getParcelable(CONTENT_DATA_KEY)
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
            //???????????????URL??????????????????????????????????????????(e.g.?????????????????????????????????????????????????????????)
            if (title.isNotEmpty() && !title.startsWith("http")) {
                //??????????????????
                this@WebActivity.binding?.title?.text = title.html2Spanned()
                //????????????????????????
                this@WebActivity.shareTitle = title.html2String()
                //??????????????????URL
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

    //????????????????????????
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_web, menu)
        if (!isArticle) {
            //????????????
            menu?.findItem(R.id.web_collect)?.isVisible = false
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        //?????????????????????????????????????????????????????????
        this.let {
            if (isCollect) {
                //?????????
                menu?.findItem(R.id.web_collect)?.icon =
                    ContextCompat.getDrawable(it, R.drawable.ic_collect_strawberry)
            } else {
                //?????????
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
                    //??????
                    item.setIcon(R.drawable.ic_collect_strawberry)
                    viewModel.collectCurrentArticle(indexId)
                        .compose(RxTransformer.async())
                        .subscribe()
                } else {
                    //????????????
                    item.setIcon(R.drawable.ic_collect_black_24)
                    viewModel.unCollectArticle(shareId, originId)
                        .compose(RxTransformer.async())
                        .subscribe()
                }
            }

            //??????
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
                //???????????????
                browse(shareUrl)
            }
            R.id.web_refresh -> {
                //??????
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
        super.onResume()
    }

    override fun onPause() {
        mAgentWeb.webLifeCycle.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        if (originId != -1 && !isCollect) {
            //????????????????????????????????????????????????????????????????????????????????????(????????????item)
            data?.let { sendEvent(it) }
        } else {
            //???????????????????????????(true)
            if (isCollect) {
                sendTag(true.toString())
            } else {
                //???????????????????????????(false)
                sendTag(false.toString())
            }
        }
        mAgentWeb.webLifeCycle.onDestroy()
        setSupportActionBar(null)
        super.onDestroy()
    }


}