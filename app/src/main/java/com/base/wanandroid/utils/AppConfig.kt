package com.base.wanandroid.utils

import com.base.wanandroid.bean.SearchHotResponse
import com.drake.serialize.serialize.serialLazy

/**
 * @author jiangshiyu
 * @date 2022/6/10
 * 本地数据配置config
 */
object AppConfig {

    //夜间主题
    var DarkTheme: Boolean by serialLazy(false)

    //登录后返回的cookie
    var Cookie: HashMap<String, String> by serialLazy(hashMapOf())

    var UserName: String by serialLazy("")

    var PassWord: String by serialLazy("")

    var Level: String by serialLazy("")

    var Rank: String by serialLazy("")

    var CoinCount: String by serialLazy("10")

    /** 搜索热词 每次打开app重新初始化赋值一次 */
    val SearchHot: MutableList<SearchHotResponse> by lazy { (mutableListOf()) }

    /** 搜索记录*/
    var SearchHistory: ArrayList<String> by serialLazy(arrayListOf())
}