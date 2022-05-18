package com.base.wanandroid.bean.base

import com.google.gson.annotations.SerializedName

/**
 * @author jiangshiyu
 * @date 2022/5/18
 */
data class ApiResponse(
    @SerializedName("errorCode")
    val errorCode: Int,
    @SerializedName("errorMsg")
    val errorMsg: String


)