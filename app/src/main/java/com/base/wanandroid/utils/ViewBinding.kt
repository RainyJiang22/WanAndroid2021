package com.base.wanandroid.utils

import android.view.View
import android.view.ViewGroup
import androidx.core.app.ComponentActivity
import androidx.viewbinding.ViewBinding

/**
 * @author jiangshiyu
 * @date 2022/3/3
 */
inline fun <reified T : ViewBinding> ComponentActivity.viewBinding(): Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) {
        val bind: (View) -> T = {
            T::class.java.getMethod("bind", View::class.java).invoke(null, it) as T
        }
        val getContentView: ComponentActivity.() -> View = {
            checkNotNull(findViewById<ViewGroup>(android.R.id.content).getChildAt(0)) {
                "Call setContentView or Use Activity's secondary constructor passing layout res id."
            }
        }
        bind(getContentView())
    }
}

fun <T : ViewBinding> ComponentActivity.viewBinding(bind: (View) -> T): Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) {
        val getContentView: ComponentActivity.() -> View = {
            checkNotNull(findViewById<ViewGroup>(android.R.id.content).getChildAt(0)) {
                "Call setContentView or Use Activity's secondary constructor passing layout res id."
            }
        }
        bind(getContentView())
    }
}

inline fun <reified T : ViewBinding> binding(view: View): T? {
    val clazz = T::class.java
    return if (ViewBinding::class.java.isAssignableFrom(clazz)
    ) {
        return clazz.getDeclaredMethod("bind", View::class.java).invoke(clazz, view) as T?
    } else {
        null
    }
}
