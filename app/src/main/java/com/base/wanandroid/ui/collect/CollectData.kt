package com.base.wanandroid.ui.collect

import com.base.wanandroid.bean.CollectListResponse
import com.base.wanandroid.bean.CollectResponse
import com.base.wanandroid.bean.UserInfoResponse
import com.base.wanandroid.bean.base.ApiPagerResponse

/**
 * @author jiangshiyu
 * @date 2022/6/19
 */
data class CollectData(
    val userInfo: UserInfoResponse?,
    val collectList: ApiPagerResponse<CollectResponse>,
)