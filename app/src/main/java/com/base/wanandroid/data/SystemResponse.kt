package com.base.wanandroid.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author jiangshiyu
 * @date 2022/9/23
 * 体系数据
 */
@Parcelize
data class SystemResponse(
    var children: ArrayList<ClassifyResponse>,
    var courseId: Int,
    var id: Int,
    var name: String,
    var order: Int,
    var parentChapterId: Int,
    var userControlSetTop: Boolean,
    var visible: Int
) : Parcelable
