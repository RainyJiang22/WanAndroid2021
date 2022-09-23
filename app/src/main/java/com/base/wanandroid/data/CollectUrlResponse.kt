package com.base.wanandroid.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author jiangshiyu
 * @date 2022/9/23
 * 收藏的网址
 */

@Parcelize
data class CollectUrlResponse(
    var icon: String,
    var id: Int,
    var link: String,
    var name: String,
    var order: Int,
    var userId: Int,
    var visible: Int
) : Parcelable
