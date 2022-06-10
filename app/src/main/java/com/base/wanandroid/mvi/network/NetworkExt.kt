package com.base.wanandroid.mvi.network

import com.blankj.utilcode.util.ToastUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun <T> Flow<T>.commonCatch(action: suspend FlowCollector<T>.(cause: Throwable) -> Unit): Flow<T> {
    return this.catch {
        if (it is UnknownHostException || it is SocketTimeoutException) {
            ToastUtils.showShort("发生网络错误，请稍后重试")
        } else {
            ToastUtils.showShort("请求失败，请重试")
        }
        action(it)
    }
}