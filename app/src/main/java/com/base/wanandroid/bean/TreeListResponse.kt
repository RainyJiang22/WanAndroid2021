package com.base.wanandroid.bean

import com.base.wanandroid.bean.base.ApiResponse
import com.example.wanAndroid.logic.model.SystemResponse
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @author jiangshiyu
 * @date 2022/3/7
 */
data class TreeListResponse(
    @SerializedName("apiResponse")
    val apiResponse: ApiResponse,

    @SerializedName("data")
    val data: List<SystemResponse>,
)