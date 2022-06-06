package com.base.wanandroid.network.entity

/**
 * @author jiangshiyu
 * @date 2022/6/6
 * 失败回调
 */
data class ApiFailedResponse<T>(
    override val errorCode: Int,
    override val errorMsg: String?
) : ApiResponse<T>(errorCode = errorCode, errorMsg = errorMsg)