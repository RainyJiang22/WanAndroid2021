package com.base.wanandroid.bean

import com.google.gson.annotations.SerializedName

/**
 * @author jiangshiyu
 * @date 2022/3/7
 */
data class BannerResponse(
    @SerializedName("baseResponse")
    val baseResponse: BaseResponse,
    @SerializedName("data")
    var data: List<BannerData>?
)