package com.base.wanandroid.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author jiangshiyu
 * @date 2022/9/23
 */

@Parcelize
data class ShareResponse(
    var coinInfo: CoinInfoResponse,
    var shareArticles: ApiPagerResponse<ArrayList<ArticleResponse>>
) : Parcelable