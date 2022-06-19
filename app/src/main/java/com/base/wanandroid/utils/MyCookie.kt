package com.base.wanandroid.utils

import android.text.TextUtils
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

/**
 * @author jiangshiyu
 * @date 2022/6/19
 * Cookie管理类
 */
class MyCookie : CookieJar {
    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        val cookies:MutableList<Cookie> = ArrayList()
        val cookieStr = loadCookie(url.host)
        if (!TextUtils.isEmpty(cookieStr)) {
            //获取所有 Cookie 字符串
            val cookieStrS = cookieStr!!.split("#").toTypedArray()
            for (aCookieStr in cookieStrS) {
                //将字符串解析成 Cookie 对象
                val cookie = Cookie.parse(url, aCookieStr)
                cookies.add(cookie!!)
            }
        }
        //返回 null 会引发异常
        return cookies
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        val cookieStr = encodeCookie(cookies)
        saveCookie(url.host, cookieStr)
    }

    private fun encodeCookie(cookies: List<Cookie>): String {
        val sb = StringBuilder()
        for (cookie in cookies) {
            //将Cookie转换成字符串
            sb.append(cookie.toString())
            //以#为分隔符
            sb.append("#")
        }
        sb.deleteCharAt(sb.lastIndexOf("#"))
        return sb.toString()
    }

    //加载cookie
    private fun loadCookie(host: String): String? {
        return if (!TextUtils.isEmpty(host) && AppConfig.Cookie.contains(host)) {
            AppConfig.Cookie[host]
        } else null
    }

    private fun saveCookie(host: String, cookie: String) {
        val cookies = AppConfig.Cookie
        if (!TextUtils.isEmpty(host)) {
            cookies[host] = cookie
        }
        AppConfig.Cookie = cookies
    }
}