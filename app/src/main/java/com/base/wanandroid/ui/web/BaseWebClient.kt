package com.base.wanandroid.ui.web

import android.net.Uri
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import com.just.agentweb.WebViewClient

/**
 * @author jiangshiyu
 * @date 2022/6/2
 * 通用web客户端
 */
class BaseWebClient : WebViewClient() {

    // 拦截的网址
    private val blackHostList = arrayListOf(
        "taobao.com",
        "jd.com",
        "alipay.com",
        "www.ilkwork.com",
        "cnzz.com",
        "yun.tuisnake.com",
        "yun.lvehaisen.com",
        "yun.tuitiger.com",
        "ad.lflucky.com",
        "downloads.jianshu.io",
        "juejin.zlink.toutiao.com",
        "app-wvhzpj.openinstall.io"
    )

    private fun isBlackHost(host: String): Boolean {
        for (blackHost in blackHostList) {
            if (blackHost == host) {
                return true
            }
        }
        return false
    }

    private fun shouldInterceptRequest(uri: Uri): Boolean {
        val host = uri.host ?: ""
        return isBlackHost(host)
    }

    override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse? {
        if (shouldInterceptRequest(request.url)) {
            return WebResourceResponse(null, null, null)
        }
        return super.shouldInterceptRequest(view, request)
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        return if (request != null) {
            val isHttp = request.url.toString().startsWith("http")
            if (!isHttp) {//fix issue #5,阻止一些非http请求（如简书：jianshu://notes/xxx)
                true
            } else {
                request.url.toString().contains("alipay")//拦截跳转支付宝
            }
        } else {
            false
        }
    }
}