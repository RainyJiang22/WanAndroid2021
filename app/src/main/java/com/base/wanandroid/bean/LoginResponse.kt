package com.base.wanandroid.bean

import com.google.gson.annotations.SerializedName

/**
 * @author jiangshiyu
 * @date 2022/3/7
 */
data class LoginResponse(
    @SerializedName("baseResponse")
    val baseResponse: BaseResponse,
    @SerializedName("data")
    var data: Data
) {
    data class Data(
        @SerializedName("id")
        var id: Int,
        @SerializedName("username")
        var username: String,
        @SerializedName("password")
        var password: String,
        @SerializedName("icon")
        var icon: String?,
        @SerializedName("type")
        var type: Int,
        @SerializedName("collectIds")
        var collectIds: List<Int>?
    )
}
