package com.base.wanandroid.application.initializer

import android.app.Application

/**
 * @author jiangshiyu
 * @date 2022/3/3
 */
abstract class DelegateInitializer @JvmOverloads constructor(private val mNext: InitialDelegate? = null) :
    InitialDelegate {

    override fun appCreate(appContext: Application) {
        onAppCreate(appContext)
        mNext?.appCreate(appContext)
    }

    protected abstract fun onAppCreate(appContext: Application)
}