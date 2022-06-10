package com.base.wanandroid.ui.user.data

import com.base.wanandroid.bean.CoinInfoResponse
import com.base.wanandroid.bean.UserInfoResponse

/**
 * @author jiangshiyu
 * @date 2022/6/10
 */
data class UserInfo(
    val userInfoResponse: UserInfoResponse?,
    val coinInfoResponse: CoinInfoResponse?
)