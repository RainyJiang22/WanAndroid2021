package com.base.wanandroid.application.initializer

import android.app.Application
import io.reactivex.functions.Consumer
import io.reactivex.plugins.RxJavaPlugins

/**
 * @author jiangshiyu
 * @date 2022/3/3
 */
class CommonProcessInitializer(mNext: InitialDelegate) : DelegateInitializer(mNext) {
    override fun onAppCreate(appContext: Application) {

        RxJavaPlugins.setErrorHandler { throwable ->
            throwable.printStackTrace()
        }
    }


}