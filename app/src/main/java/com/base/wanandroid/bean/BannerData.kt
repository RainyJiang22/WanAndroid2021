package com.base.wanandroid.bean

import com.google.gson.annotations.SerializedName

/**
 * @author jiangshiyu
 * @date 2022/3/8
 */
data class BannerData(
    @SerializedName("id")
    var id: Int,
    @SerializedName("url")
    var url: String,
    @SerializedName("imagePath")
    var imagePath: String,
    @SerializedName("title")
    var title: String,
    @SerializedName("desc")
    var desc: String,
    @SerializedName("isVisible")
    var isVisible: Int,
    @SerializedName("order")
    var order: Int,
    @SerializedName("type")
    var `type`: Int
)
