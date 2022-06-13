package com.base.wanandroid.utils

import com.drake.serialize.serialize.serialLazy

/**
 * @author jiangshiyu
 * @date 2022/6/10
 * 本地数据配置config
 */
object AppConfig {

    var Cookie: HashMap<String, String> by serialLazy(hashMapOf())

    var UserName: String by serialLazy("")

    var PassWord: String by serialLazy("")

    var Level: String by serialLazy("")

    var Rank: String by serialLazy("")

    var CoinCount: String by serialLazy("")
}