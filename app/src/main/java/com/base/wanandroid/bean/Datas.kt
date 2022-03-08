package com.base.wanandroid.bean

import com.google.gson.annotations.SerializedName

/**
 * @author jiangshiyu
 * @date 2022/3/7
 */
data class Datas(
    @SerializedName("id")
    var id: Int,
    @SerializedName("originId")
    var originId: Int,
    @SerializedName("title")
    var title: String,
    @SerializedName("chapterId")
    var chapterId: Int,
    @SerializedName("chapterName")
    var chapterName: String?,
    @SerializedName("envelopePic")
    var envelopePic: Any,
    @SerializedName("link")
    var link: String,
    @SerializedName("author")
    var author: String,
    @SerializedName("origin")
    var origin: Any,
    @SerializedName("publishTime")
    var publishTime: Long,
    @SerializedName("zan")
    var zan: Any,
    @SerializedName("desc")
    var desc: Any,
    @SerializedName("visible")
    var visible: Int,
    @SerializedName("niceData")
    var niceDate: String,
    @SerializedName("courseId")
    var courseId: Int,
    @SerializedName("collect")
    var collect: Boolean
)