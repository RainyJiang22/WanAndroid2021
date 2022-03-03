package com.base.wanandroid.application

import android.app.Application
import android.content.Context
import com.base.wanandroid.application.initializer.CommonProcessInitializer
import com.base.wanandroid.application.initializer.DelegateInitializer
import com.base.wanandroid.application.initializer.MainProcessInitializer
import com.base.wanandroid.application.initializer.StubProcessInitializer
import com.base.wanandroid.utils.AppUtils

/**
 * @author jiangshiyu
 * @date 2022/3/3
 */
class WanAndroidApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }


    override fun onCreate() {
        super.onCreate()
        sContext = this

        mInitializer = if(AppUtils.inMainProcess(this)) {
            CommonProcessInitializer(newMainProcessInitializer())
        } else {
            CommonProcessInitializer(newStubProcessInitializer())
        }
        mInitializer?.appCreate(this)

    }

    private fun newStubProcessInitializer(): DelegateInitializer {
        return StubProcessInitializer()
    }

    private fun newMainProcessInitializer(): DelegateInitializer {
        return MainProcessInitializer()
    }


    companion object {
        private var sContext: Context? = null

        @JvmStatic
        fun getApplication(): Context? {
            return sContext
        }

        private var mInitializer: CommonProcessInitializer? = null
    }
}