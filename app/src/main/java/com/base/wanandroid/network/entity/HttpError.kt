package com.base.wanandroid.network.entity

import com.google.gson.JsonParseException
import retrofit2.HttpException
import java.net.SocketTimeoutException

/**
 * @author jiangshiyu
 * @date 2022/6/6
 */
class HttpError {

    internal fun handleException(e: Throwable): String = when (e) {
        is HttpException -> {
            "网络连接失败"
        }
        is SocketTimeoutException -> {
            "网络连接超时"
        }
        is JsonParseException -> {
            "数据解析失败"
        }
        else -> {
            "其他"
        }
    }
}