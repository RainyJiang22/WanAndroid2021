package com.base.wanandroid.bean

import com.base.wanandroid.bean.base.ApiResponse
import com.google.gson.annotations.SerializedName

/**
 * @author jiangshiyu
 * @date 2022/5/26
 * 积分信息数据
 */
data class CoinInfoResponse(


    @SerializedName("api_response")
    val baseResponse: ApiResponse,

    @SerializedName("coinCount")
    val coinCount: Int,
    @SerializedName("level")
    val level: Int,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("rank")
    val rank: String,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("username")
    val username: String
)