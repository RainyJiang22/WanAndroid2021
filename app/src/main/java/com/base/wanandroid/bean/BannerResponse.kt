package com.base.wanandroid.bean

/**
 * @author jiangshiyu
 * @date 2022/3/7
 */
data class BannerResponse(
    val baseResponse: BaseResponse,
    var data: List<Data>?
) {
    data class Data(
        var id: Int,
        var url: String,
        var imagePath: String,
        var title: String,
        var desc: String,
        var isVisible: Int,
        var order: Int,
        var `type`: Int
    )
}