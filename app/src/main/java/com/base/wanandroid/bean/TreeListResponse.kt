package com.base.wanandroid.bean

import java.io.Serializable

/**
 * @author jiangshiyu
 * @date 2022/3/7
 */
data class TreeListResponse(
    var baseResponse: BaseResponse,
    var data: List<Data>
) {
    data class Data(
        var id: Int,
        var name: String,
        var courseId: Int,
        var parentChapterId: Int,
        var order: Int,
        var visible: Int,
        var children: List<Children>?
    ) : Serializable {
        data class Children(
            var id: Int,
            var name: String,
            var courseId: Int,
            var parentChapterId: Int,
            var order: Int,
            var visible: Int,
            var children: List<Children>?
        ) : Serializable
    }
}