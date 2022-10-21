package com.base.wanandroid.application.initializer

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.base.wanandroid.R
import com.base.wanandroid.utils.AppConfig
import com.base.wanandroid.widget.loadcallback.EmptyCallBack
import com.base.wanandroid.widget.loadcallback.ErrorCallBack
import com.base.wanandroid.widget.loadcallback.LoadingCallBack
import com.base.wanandroid.widget.refresh.MyClassicFooter
import com.base.wanandroid.widget.refresh.MyClassicHeader
import com.drake.brv.PageRefreshLayout
import com.drake.statelayout.StateConfig
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadSir
import com.rainy.easybus.EventBusInitializer
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tencent.mmkv.MMKV

/**
 * @author jiangshiyu
 * @date 2022/3/3
 */
class MainProcessInitializer : DelegateInitializer() {

    override fun onAppCreate(appContext: Application) {
        MMKV.initialize(appContext)
        //应用主题切换
        when (AppConfig.DarkTheme) {
            true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
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

        //界面管理 初始化
        LoadSir.beginBuilder()
            .addCallback(LoadingCallBack())//加载
            .addCallback(ErrorCallBack())//错误
            .addCallback(EmptyCallBack())//空
            .setDefaultCallback(SuccessCallback::class.java)//设置默认加载状态页
            .commit()

        EventBusInitializer.init(appContext)
    }


    companion object {
        private val TAG = "MainProcessInitializer"
    }
}