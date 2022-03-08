package com.base.wanandroid.bean

import com.google.gson.annotations.SerializedName

/**
 * @author jiangshiyu
 * @date 2022/3/7
 */
data class HomeListResponse(

    @SerializedName("baseResponse")
    val baseResponse: BaseResponse,

    @SerializedName("data")
    val data: Data
)