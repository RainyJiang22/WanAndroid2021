package com.example.wanAndroid.logic.model

import com.base.wanandroid.bean.ClassificationResponse
import com.google.gson.annotations.SerializedName

/** 体系数据类 */
data class SystemResponse(

    @SerializedName("children")
    val children: MutableList<ClassificationResponse>,
    @SerializedName("courseId")
    val courseId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)
