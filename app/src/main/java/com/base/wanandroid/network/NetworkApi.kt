package com.base.wanandroid.network

import com.base.wanandroid.network.api.WanApiService
import com.base.wanandroid.widget.refresh.MyClassicFooter
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.google.gson.GsonBuilder
import me.hgj.jetpackmvvm.base.appContext
import me.hgj.jetpackmvvm.network.BaseNetworkApi
import me.hgj.jetpackmvvm.network.interceptor.CacheInterceptor
import me.hgj.jetpackmvvm.network.interceptor.logging.LogInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * @author jiangshiyu
 * @date 2022/9/26
 */

//双重校验
val apiService: WanApiService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    NetworkApi.INSTANCE.getApi(WanApiService::class.java, WanApiService.SERVER_URL)
}

class NetworkApi : BaseNetworkApi() {

    companion object {
        val INSTANCE: NetworkApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetworkApi()
        }
    }

    val cookieJar: PersistentCookieJar by lazy {
        PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(appContext))
    }

    override fun setHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        builder.apply {
            //设置缓存配置,最大10M
            cache(Cache(File(appContext.cacheDir, "cxk_cache"), 10 * 1024 * 1024))
            //cookies自动持久化
            cookieJar(cookieJar)
            //添加缓存拦截器 可传入缓存天数，不传默认7天
            addInterceptor(CacheInterceptor())
            // 日志拦截器
            addInterceptor(LogInterceptor())
            //超时时间 连接、读、写
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
        }
        return builder
    }

    /**
     * 实现重写父类的setRetrofitBuilder
     */
    override fun setRetrofitBuilder(builder: Retrofit.Builder): Retrofit.Builder {
        return builder.apply {
            addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        }
    }

}