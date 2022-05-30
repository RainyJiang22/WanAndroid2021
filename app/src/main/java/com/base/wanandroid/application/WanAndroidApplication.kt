package com.base.wanandroid.application

import android.app.Application
import android.content.Context
import android.graphics.pdf.PdfDocument
import com.base.wanandroid.R
import com.base.wanandroid.application.initializer.CommonProcessInitializer
import com.base.wanandroid.application.initializer.DelegateInitializer
import com.base.wanandroid.application.initializer.MainProcessInitializer
import com.base.wanandroid.application.initializer.StubProcessInitializer
import com.base.wanandroid.utils.AppUtils
import com.drake.brv.PageRefreshLayout
import com.drake.statelayout.StateConfig
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout

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

        mInitializer = if (AppUtils.inMainProcess(this)) {
            CommonProcessInitializer(newMainProcessInitializer())
        } else {
            CommonProcessInitializer(newStubProcessInitializer())
        }
        mInitializer?.appCreate(this)


        //全局缺省页配置
        StateConfig.apply {
            emptyLayout = R.layout.layout_empty
            loadingLayout = R.layout.layout_loading
            errorLayout = R.layout.layout_error
        }

        //全局分页索引
        PageRefreshLayout.startIndex = 0
        //全局预加载索引
        PageRefreshLayout.preloadIndex = 3

        //初始化SmartRefreshLayout构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setPrimaryColorsId(R.color.color_primary_dark, R.color.white)
            ClassicsHeader(context)
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
            ClassicsFooter(context).setDrawableSize(20f)
        }


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