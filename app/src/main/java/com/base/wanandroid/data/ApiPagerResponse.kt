package com.base.wanandroid.data

import java.io.Serializable

/**
 * @author jiangshiyu
 * @date 2022/9/23
 * 分类数据的基类
 */
data class ApiPagerResponse<T>(
    var datas: T,
    var curPage: Int,
    var offset: Int,
    var over: Boolean,
    var pageCount: Int,
    var size: Int,
    var total: Int
) : Serializable {

    /**
     * 数据是否为空
     */
    fun isEmpty() =  (datas as List<*>).size == 0

    /**
     * 是否为刷新
     */
    fun isRefresh() = offset == 0

    /**
     * 是否还有更多数据
     */
    fun hasMore() = !over
}