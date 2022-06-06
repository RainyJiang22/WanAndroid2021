package com.base.wanandroid.network.entity

import java.io.Serializable

/**
 * @author jiangshiyu
 * @date 2022/6/6
 *
 * 这里不用data class
 */
open class ApiResponse<T>(
    open val data: T? = null,
    open val errorCode: Int? = null,
    open val errorMsg: String? = null,
    open val error: Throwable? = null,
) : Serializable {
    val isSuccess
        get() = errorCode == 0
}