package com.base.wanandroid.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author jiangshiyu
 * @date 2022/9/23
 * 导航数据
 */

@Parcelize
data class NavigationResponse(
    var articles: ArrayList<ArticleResponse>,
    var cid: Int,
    var name: String
) : Parcelable