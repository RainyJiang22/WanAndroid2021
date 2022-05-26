package com.base.wanandroid.bean

import com.google.gson.annotations.SerializedName

/**
 * @author jiangshiyu
 * @date 2022/5/26
 */
data class ClassificationResponse(

    @SerializedName("courseId")
    val courseId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)