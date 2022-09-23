package com.base.wanandroid.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author jiangshiyu
 * @date 2022/9/23
 * 搜索热词
 */
@Parcelize
data class SearchResponse(
    var id: Int,
    var link: String,
    var name: String,
    var order: Int,
    var visible: Int
) : Parcelable