package com.base.wanandroid.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author jiangshiyu
 * @date 2022/9/23
 * 分享人信息
 */
@Parcelize
data class CoinInfoResponse(
    var coinCount: Int,
    var rank: String,
    var userId: Int,
    var username: String
) : Parcelable