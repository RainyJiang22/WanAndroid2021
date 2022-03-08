package com.base.wanandroid.bean

import com.google.gson.annotations.SerializedName

/**
 * @author jiangshiyu
 * @date 2022/3/7
 */
data class HotKeyResponse(
    @SerializedName("baseResponse")
    var baseResponse: BaseResponse,
    @SerializedName("data")
    var data: List<Data>?
) {
    data class Data(
        @SerializedName("id")
        var id: Int,
        @SerializedName("name")
        var name: String,
        @SerializedName("link")
        var link: Any,
        @SerializedName("visible")
        var visible: Int,
        @SerializedName("order")
        var order: Int
    )
}