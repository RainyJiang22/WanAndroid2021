package com.base.wanandroid.bean

import com.base.wanandroid.bean.base.ApiResponse
import com.google.gson.annotations.SerializedName

/**
 * @author jiangshiyu
 * @date 2022/6/16
 */
data class ShareListResponse(

    @SerializedName("apiResponse")
    val apiResponse: ApiResponse,

    @SerializedName("data")
    val data: ShareResponse,
)
