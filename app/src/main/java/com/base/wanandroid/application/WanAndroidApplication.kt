package com.base.wanandroid.application

import android.app.Application
import android.content.Context
import com.base.wanandroid.application.initializer.CommonProcessInitializer
import com.base.wanandroid.application.initializer.DelegateInitializer
import com.base.wanandroid.application.initializer.MainProcessInitializer
import com.base.wanandroid.application.initializer.StubProcessInitializer
import com.base.wanandroid.base.BaseApp
import com.base.wanandroid.utils.AppUtils
import com.base.wanandroid.viewmodel.state.AppViewModel

/**
 * @author jiangshiyu
 * @date 2022/3/3
 */

val appViewModel: AppViewModel by lazy { WanAndroidApplication.appViewModelInstance }


class WanAndroidApplication : BaseApp() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }


    override fun onCreate() {
        super.onCreate()
        sContext = this

        mInitializer = if (AppUtils.inMainProcess(this)) {
            CommonProcessInitializer(newMainProcessInitializer())
        } else {
            CommonProcessInitializer(newStubProcessInitializer())
        }
        mInitializer?.appCreate(this)
        appViewModelInstance = getAppViewModelProvider()[AppViewModel::class.java]

    }

    private fun newStubProcessInitializer(): DelegateInitializer {
        return StubProcessInitializer()
    }

    private fun newMainProcessInitializer(): DelegateInitializer {
        return MainProcessInitializer()
    }


    companion object {
        private var sContext: Context? = null


        //根据昵称生成头像API
        const val apiKey = "MDgWZ4B65I9G3K"

        @JvmStatic
        fun getApplication(): Context? {
            return sContext
        }

        private var mInitializer: CommonProcessInitializer? = null

        lateinit var appViewModelInstance: AppViewModel
    }
}