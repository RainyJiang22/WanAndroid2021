package com.base.wanandroid.application

import android.app.Application
import android.content.Context
import android.graphics.pdf.PdfDocument
import androidx.appcompat.app.AppCompatDelegate
import com.base.wanandroid.R
import com.base.wanandroid.application.initializer.CommonProcessInitializer
import com.base.wanandroid.application.initializer.DelegateInitializer
import com.base.wanandroid.application.initializer.MainProcessInitializer
import com.base.wanandroid.application.initializer.StubProcessInitializer
import com.base.wanandroid.network.GsonConvert
import com.base.wanandroid.utils.AppConfig
import com.base.wanandroid.utils.AppUtils
import com.base.wanandroid.utils.BASE_URL
import com.base.wanandroid.utils.MyCookie
import com.base.wanandroid.widget.refresh.MyClassicFooter
import com.base.wanandroid.widget.refresh.MyClassicHeader
import com.drake.brv.PageRefreshLayout
import com.drake.net.NetConfig
import com.drake.net.okhttp.setConverter
import com.drake.statelayout.StateConfig
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import retrofit2.converter.gson.GsonConverterFactory

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

        //应用主题切换
        when (AppConfig.DarkTheme) {
            true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        NetConfig.initialize(BASE_URL) {
            //设置Gson解析方式
            setConverter(GsonConvert())
            //设置cookie管理器
            cookieJar(MyCookie())
        }

        //全局缺省页配置
        StateConfig.apply {
            emptyLayout = R.layout.layout_empty
            loadingLayout = R.layout.layout_loading
            errorLayout = R.layout.layout_error
            //重试
            setRetryIds(R.id.msg)
        }

        //全局分页索引
        PageRefreshLayout.startIndex = 0
        //全局预加载索引
        PageRefreshLayout.preloadIndex = 3

        //初始化SmartRefreshLayout构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            MyClassicHeader(context)
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
            MyClassicFooter(context)
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


        //根据昵称生成头像API
        const val apiKey = "MDgWZ4B65I9G3K"

        @JvmStatic
        fun getApplication(): Context? {
            return sContext
        }

        private var mInitializer: CommonProcessInitializer? = null
    }
}