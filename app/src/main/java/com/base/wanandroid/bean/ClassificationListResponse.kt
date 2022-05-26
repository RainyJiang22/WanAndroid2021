package com.base.wanandroid.bean

import com.base.wanandroid.bean.base.ApiPagerResponse
import com.base.wanandroid.bean.base.ApiResponse
import com.google.gson.annotations.SerializedName

/**
 * @author jiangshiyu
 * @date 2022/5/26
 * 列表分类数据
 */
data class ClassificationListResponse(

    @SerializedName("apieResponse")
    val apiResponse: ApiResponse,

    @SerializedName("data")
    val data: ApiPagerResponse<ClassificationResponse>
)