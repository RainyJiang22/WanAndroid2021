package com.base.wanandroid.utils

import com.tencent.mmkv.MMKV

/**
 * @author jiangshiyu
 * @date 2022/9/27
 */
object CacheUtil {


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