package com.base.wanandroid.utils

import com.base.wanandroid.data.SearchResponse
import com.drake.serialize.serialize.serialLazy

/**
 * @author jiangshiyu
 * @date 2022/6/10
 * 本地数据配置config
 */
object AppConfig {

    //夜间主题
    var DarkTheme: Boolean by serialLazy(false)


    var CoinCount: String by serialLazy("10")

    /** 搜索热词 每次打开app重新初始化赋值一次 */
    val SearchHot: MutableList<SearchResponse> by lazy { (mutableListOf()) }

    /** 搜索记录*/
    var SearchHistory: ArrayList<String> by serialLazy(arrayListOf())
}