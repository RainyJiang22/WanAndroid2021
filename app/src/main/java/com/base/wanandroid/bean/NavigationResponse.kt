package com.base.wanandroid.bean

import com.google.gson.annotations.SerializedName

/**
 * @author jiangshiyu
 * @date 2022/5/26
 * 导航数据类
 */
data class NavigationResponse(

    @SerializedName("articles")
    val articles: MutableList<ArticleResponse>,
    @SerializedName("cid")
    val cid: Int,
    @SerializedName("name")
    val name: String
)
