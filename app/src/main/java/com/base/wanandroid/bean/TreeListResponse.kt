package com.base.wanandroid.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @author jiangshiyu
 * @date 2022/3/7
 */
data class TreeListResponse(
    @SerializedName("baseResponse")
    var baseResponse: BaseResponse,
    @SerializedName("data")
    var data: List<Data>
) {
    data class Data(
        @SerializedName("id")
        var id: Int,
        @SerializedName("name")
        var name: String,
        @SerializedName("courseId")
        var courseId: Int,
        @SerializedName("parentChapterId")
        var parentChapterId: Int,
        @SerializedName("order")
        var order: Int,
        @SerializedName("visible")
        var visible: Int,
        @SerializedName("children")
        var children: List<Children>?
    ) : Serializable {
        data class Children(
            @SerializedName("id")
            var id: Int,
            @SerializedName("name")
            var name: String,
            @SerializedName("courseId")
            var courseId: Int,
            @SerializedName("parentChapterId")
            var parentChapterId: Int,
            @SerializedName("order")
            var order: Int,
            @SerializedName("visible")
            var visible: Int,
            @SerializedName("children")
            var children: List<Children>?
        ) : Serializable
    }
}