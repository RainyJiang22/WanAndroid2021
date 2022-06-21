package com.base.wanandroid.mvi

import com.base.wanandroid.mvi.repository.ArticleListResponse
import com.base.wanandroid.utils.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

/**
 * @author jiangshiyu
 * @date 2022/6/10
 */
interface WanApi {

    companion object {
        fun create(): WanApi {
            val okHttpClient = OkHttpClient()
                .newBuilder()
                .retryOnConnectionFailure(true)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WanApi::class.java)
        }

    }


    @GET("/article/list/{page}/json")
    suspend fun getArticleList(@Path("page") page: Int): ArticleListResponse


}