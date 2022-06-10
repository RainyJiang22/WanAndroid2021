package com.base.wanandroid.mvi.repository

import com.base.wanandroid.bean.ArticleResponse
import com.base.wanandroid.bean.base.ApiPagerResponse
import com.base.wanandroid.bean.base.ApiResponse
import com.google.gson.annotations.SerializedName

/**
 * @author jiangshiyu
 * @date 2022/6/10
 */
data class ArticleListResponse(
    @SerializedName("apiResponse")
    val apiResponse: ApiResponse,
    @SerializedName("data")
    val data: ApiPagerResponse<ArticleResponse>
)