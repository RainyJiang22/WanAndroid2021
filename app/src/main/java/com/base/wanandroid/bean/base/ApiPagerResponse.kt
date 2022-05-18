package com.base.wanandroid.bean.base

import com.base.wanandroid.bean.Datas
import com.google.gson.annotations.SerializedName

/**
 * @author jiangshiyu
 * @date 2022/5/18
 */
data class ApiPagerResponse<T>(
    @SerializedName("curPage")
    val curPage: Int,
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("over")
    val over: Boolean,
    @SerializedName("pageCount")
    val pageCount: Int,
    @SerializedName("size")
    val size: Int,
    @SerializedName("total")
    val total: Int,
    @SerializedName("datas")
    val datas: List<T>
)