package com.base.wanandroid.bean

import com.google.gson.annotations.SerializedName

/**
 * @author jiangshiyu
 * @date 2022/3/7
 */
data class FriendListResponse(
    @SerializedName("baseResponse")
    var baseResponse: BaseResponse,
    @SerializedName("data")
    var data: List<Data>?
) {
    data class Data(
        var id: Int,
        var name: String,
        var link: String,
        var visible: Int,
        var order: Int,
        var icon: Any
    )
}