package com.base.wanandroid.bean

/**
 * @author jiangshiyu
 * @date 2022/3/7
 */
data class LoginResponse(
    val baseResponse: BaseResponse,
    var data: Data
) {
    data class Data(
        var id: Int,
        var username: String,
        var password: String,
        var icon: String?,
        var type: Int,
        var collectIds: List<Int>?
    )
}
