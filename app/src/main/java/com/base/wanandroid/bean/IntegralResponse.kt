package com.base.wanandroid.bean

import com.google.gson.annotations.SerializedName

/**
 * @author jiangshiyu
 * @date 2022/5/26
 * 积分获取列表数据
 */
data class IntegralResponse(
    @SerializedName("coinCount")
    val coinCount: Int,
    @SerializedName("date")
    val date: Long,
    @SerializedName("desc")
    val desc: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("reason")
    val reason: String,
    @SerializedName("type")
    val type: Int,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("username")
    val userName: String
)