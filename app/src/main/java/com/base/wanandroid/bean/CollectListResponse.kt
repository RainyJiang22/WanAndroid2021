package com.base.wanandroid.bean

import com.base.wanandroid.bean.base.ApiPagerResponse
import com.base.wanandroid.bean.base.ApiResponse
import com.google.gson.annotations.SerializedName

/**
 * @author jiangshiyu
 * @date 2022/5/26
 * 收藏列表
 */
data class CollectListResponse(

    @SerializedName("api_response")
    val apiResponse: ApiResponse,

    @SerializedName("data")
    val data: ApiPagerResponse<CollectResponse>
)