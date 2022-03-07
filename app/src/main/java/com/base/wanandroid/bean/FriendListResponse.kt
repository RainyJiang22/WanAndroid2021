package com.base.wanandroid.bean

/**
 * @author jiangshiyu
 * @date 2022/3/7
 */
data class FriendListResponse(
    var baseResponse: BaseResponse,
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