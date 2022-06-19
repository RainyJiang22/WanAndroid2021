package com.base.wanandroid.network

import com.base.wanandroid.constant.Constant
import com.base.wanandroid.network.api.WanApi
import com.base.wanandroid.utils.MyCookie
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author jiangshiyu
 * @date 2022/3/3
 */
object RetrofitHelper {

    private val wanApi by lazy {
        Retrofit.Builder()
            .baseUrl(Constant.URI.BASE_URI)
            .client(getClient())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//支持rxjava
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WanApi::class.java)
    }

    private fun getClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .cookieJar(MyCookie())
            .retryOnConnectionFailure(true)
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }


    fun get(): WanApi {
        return wanApi
    }


}