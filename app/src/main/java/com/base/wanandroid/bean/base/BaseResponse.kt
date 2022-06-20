package com.base.wanandroid.bean.base

/**
 * @author jiangshiyu
 * @date 2022/6/20
 */
data class BaseResponse<T>(
    val errorCode: Int,
    val errorMsg: String,
    val data: T
)