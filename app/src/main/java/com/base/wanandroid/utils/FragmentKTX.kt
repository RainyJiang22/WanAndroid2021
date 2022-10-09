package com.base.wanandroid.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

/**
 * @author jiangshiyu
 * @date 2022/9/8
 */

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) =
    beginTransaction().func().commitAllowingStateLoss()

fun FragmentActivity.addFragment(fragment: Fragment, frameId: Int) =
    supportFragmentManager.inTransaction { add(frameId, fragment) }

fun FragmentActivity.showFragment(fragment: Fragment, frameId: Int) =
    supportFragmentManager.inTransaction { replace(frameId, fragment) }

fun Fragment.showFragment(fragment: Fragment, frameId: Int) =
    childFragmentManager.inTransaction { replace(frameId, fragment) }

fun FragmentActivity.closeFragment(fragment: Fragment?): Boolean {
    fragment?.let {
        if (!it.isHidden) {
            supportFragmentManager.inTransaction { remove(it) }
            return true
        } else {
            return false
        }
    }
    return false
}

fun Fragment.closeFragment(fragment: Fragment?): Boolean {
    fragment?.let {
        if (!it.isHidden) {
            childFragmentManager.inTransaction { remove(it) }
            return true
        } else {
            return false
        }
    }
    return false
}
