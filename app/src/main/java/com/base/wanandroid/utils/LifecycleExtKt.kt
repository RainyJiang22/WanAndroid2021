package com.base.wanandroid.utils

import android.app.Dialog
import androidx.lifecycle.LifecycleOwner
import io.reactivex.disposables.Disposable
import java.lang.IllegalArgumentException

/**
 * @author jiangshiyu
 * @date 2022/5/18
 */
fun Dialog.lifecycleOwner(owner: LifecycleOwner? = null): Dialog {
    val observer = ClearLifecycleObserver(this::dismiss)
    val lifecycleOwner = owner ?: (context as? LifecycleOwner
        ?: throw IllegalStateException(
            "$context is not a LifecycleOwner."
        ))
    lifecycleOwner.lifecycle.addObserver(observer)
    return this
}


fun Disposable.lifecycleOwner(owner: LifecycleOwner): Disposable {
    val observer = ClearLifecycleObserver(this::dispose)
    owner.lifecycle.addObserver(observer)
    return this
}
