package com.base.wanandroid.application.initializer

import android.app.Application

/**
 * @author jiangshiyu
 * @date 2022/3/3
 */
interface InitialDelegate {

    fun appCreate(appContext: Application)
}