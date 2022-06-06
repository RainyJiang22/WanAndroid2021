package com.base.wanandroid.network.entity

/**
 * @author jiangshiyu
 * @date 2022/6/6
 * 成功回调
 */
data class ApiSuccessResponse<T>(val response: T) : ApiResponse<T>(data = response)