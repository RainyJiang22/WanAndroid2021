package com.base.wanandroid.utils

import android.text.TextUtils
import com.base.wanandroid.data.UserInfo
import com.google.gson.Gson
import com.tencent.mmkv.MMKV

/**
 * @author jiangshiyu
 * @date 2022/9/27
 */
object CacheUtil {
    /**
     * 获取保存的账户信息
     */
    fun getUser(): UserInfo? {
        val kv = MMKV.mmkvWithID("app")
        val userStr = kv?.decodeString("user")
        return if (TextUtils.isEmpty(userStr)) {
            null
        } else {
            Gson().fromJson(userStr, UserInfo::class.java)
        }
    }

    /**
     * 设置账户信息
     */
    fun setUser(userResponse: UserInfo?) {
        val kv = MMKV.mmkvWithID("app")
        if (userResponse == null) {
            kv?.encode("user", "")
            setIsLogin(false)
        } else {
            kv?.encode("user", Gson().toJson(userResponse))
            setIsLogin(true)
        }

    }

    /**
     * 是否已经登录
     */
    fun isLogin(): Boolean {
        val kv = MMKV.mmkvWithID("app")
        return kv?.decodeBool("login", false) == true
    }

    /**
     * 设置是否已经登录
     */
    fun setIsLogin(isLogin: Boolean) {
        val kv = MMKV.mmkvWithID("app")
        kv?.encode("login", isLogin)
    }

    /**
     * 是否是第一次登陆
     */
    fun isFirst(): Boolean? {
        val kv = MMKV.mmkvWithID("app")
        return kv?.decodeBool("first", true)
    }

    /**
     * 是否是第一次登陆
     */
    fun setFirst(first: Boolean): Boolean? {
        val kv = MMKV.mmkvWithID("app")
        return kv?.encode("first", first)
    }


    /**
     * 首页是否开启获取指定文章
     */
    fun isNeedTop(): Boolean? {
        val kv = MMKV.mmkvWithID("app")
        return kv?.decodeBool("top", true)
    }

    /**
     * 设置首页是否开启获取指定文章
     */
    fun setIsNeedTop(isNeedTop: Boolean): Boolean? {
        val kv = MMKV.mmkvWithID("app")
        return kv?.encode("top", isNeedTop)
    }
}