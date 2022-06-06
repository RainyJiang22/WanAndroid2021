package com.base.wanandroid.network.entity

/**
 * @author jiangshiyu
 * @date 2022/6/6
 * 错误回调
 */
data class ApiErrorResponse<T>(val throwable: Throwable) : ApiResponse<T>(error = throwable)