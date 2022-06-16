package com.base.wanandroid.bean

import com.base.wanandroid.bean.base.ApiPagerResponse
import com.base.wanandroid.bean.base.ApiResponse
import com.google.gson.annotations.SerializedName


/** 分享列表数据类 */
data class ShareResponse(

    @SerializedName("shareArticles")
    val shareArticles: ApiPagerResponse<ArticleResponse>
)