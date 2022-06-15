package com.base.wanandroid.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * @author jiangshiyu
 * @date 2022/5/26
 * 收藏数据
 */
@Parcelize
data class CollectResponse(

    @SerializedName("author")
    val author: String,
    @SerializedName("chapterId")
    val chapterId: Int,
    @SerializedName("chapterName")
    val chapterName: String,
    @SerializedName("courseId")
    val courseId: Int,
    @SerializedName("desc")
    val desc: String,
    @SerializedName("envelopePic")
    val envelopePic: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("link")
    val link: String,
    @SerializedName("niceDate")
    val niceDate: String,
    @SerializedName("origin")
    val origin: String,
    @SerializedName("originId")
    val originId: Int,
    @SerializedName("publishTime")
    val publishTime: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("userId")
    val userId: Int,
) : Parcelable