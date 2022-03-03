package com.base.wanandroid.application.initializer

import android.app.Application

/**
 * @author jiangshiyu
 * @date 2022/3/3
 */
class MainProcessInitializer : DelegateInitializer() {

    override fun onAppCreate(appContext: Application) {


    }


    companion object {
        private val TAG = "MainProcessInitializer"
    }
}