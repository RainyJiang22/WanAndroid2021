package com.base.wanandroid.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * @author jiangshiyu
 * @date 2022/6/10
 */

const val BASE_URL = "https://www.wanandroid.com"

//LCE -> loading/content/error
sealed class PageState<out T> {
    data class Success<T>(val data: T) : PageState<T>()
    data class Error<T>(val message: String) : PageState<T>() {
        constructor(t: Throwable) : this(t.message ?: "")
    }
}

fun <T> MutableLiveData<T>.asLiveData(): LiveData<T> {
    return this
}

sealed class FetchStatus {
    object Fetching : FetchStatus()
    object Fetched : FetchStatus()
    object NotFetched : FetchStatus()
}
