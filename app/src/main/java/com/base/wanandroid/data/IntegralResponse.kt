package com.base.wanandroid.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author jiangshiyu
 * @date 2022/9/23
 */

@Parcelize
data class IntegralResponse(
    var coinCount: Int,//当前积分
    var rank: Int,
    var userId: Int,
    var username: String
) : Parcelable