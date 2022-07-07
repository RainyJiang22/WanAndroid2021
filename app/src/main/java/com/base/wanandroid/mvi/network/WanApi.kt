package com.base.wanandroid.mvi.network

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


    @GET("/article/list/{page}/json")
    suspend fun getArticleList(@Path("page") page: Int): ArticleListResponse



}