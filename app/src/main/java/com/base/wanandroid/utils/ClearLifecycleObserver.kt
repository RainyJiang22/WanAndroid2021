package com.base.wanandroid.utils

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner


/**
 * @author jiangshiyu
 * @date 2022/5/18
 */
internal class ClearLifecycleObserver(private val dismiss: () -> Unit) : DefaultLifecycleObserver {

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        owner.lifecycle.removeObserver(this)
        dismiss()
    }
}
