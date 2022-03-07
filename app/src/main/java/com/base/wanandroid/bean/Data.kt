package com.base.wanandroid.bean

/**
 * @author jiangshiyu
 * @date 2022/3/7
 */
data class Data(
    val curPage: Int,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int,
    val datas: Datas
)