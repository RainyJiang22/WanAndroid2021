package com.base.wanandroid.network.api

import com.base.wanandroid.bean.ArticleListResponse
import com.base.wanandroid.bean.BannerResponse
import com.base.wanandroid.bean.FriendListResponse
import com.base.wanandroid.bean.HomeListResponse
import com.base.wanandroid.bean.HotKeyResponse
import com.base.wanandroid.bean.LoginResponse
import com.base.wanandroid.bean.TreeListResponse
import com.base.wanandroid.constant.Constant
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author jiangshiyu
 * @date 2022/3/3
 */
interface WanApi {


    /**
     * 首页数据
     * https://www.wanandroid.com/article/list/0/json
     * @param page page
     */
    @GET(Constant.URI.HOME_LIST)
    fun getHomeList(@Path("page") page: Int): Observable<HomeListResponse>


    /**
     * 首页文章列表
     * https://www.wanandroid.com/article/list/0/json
     */
    @GET(Constant.URI.HOME_LIST)
    fun getArticleList(@Path("page") page: Int): Observable<ArticleListResponse>

    /**
     * 知识体系
     * https://www.wanandroid.com/tree/json
     */
    @GET(Constant.URI.TREE_LIST)
    fun getTypeTreeList(): Observable<TreeListResponse>

    /**
     * 知识体系下的文章
     * https://www.wanandroid.com/article/list/0/json?cid=168
     * @param page page
     * @param cid cid
     */
    @GET(Constant.URI.TREE_ARTICLE_LIST)
    fun getTreeArticleList(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): Observable<ArticleListResponse>

    /**
     * 常用网站
     * https://www.wanandroid.com/friend/json
     */
    @GET(Constant.URI.FRIEND_LIST)
    fun getFriendList(): Observable<FriendListResponse>

    /**
     * 大家都在搜
     * https://www.wanandroid.com/hotkey/json
     */
    @GET(Constant.URI.HOT_KEY)
    fun getHotKeyList(): Observable<HotKeyResponse>

    /**
     * 搜索
     * http://www.wanandroid.com/article/query/0/json
     * @param page page
     * @param k POST search key
     */
    @POST(Constant.URI.SEARCH)
    @FormUrlEncoded
    fun getSearchList(
        @Path("page") page: Int,
        @Field("k") k: String
    ): Observable<HomeListResponse>

    /**
     * 登录
     * @param username username
     * @param password password
     * @return Deferred<LoginResponse>
     */
    @POST(Constant.URI.LOGIN)
    @FormUrlEncoded
    fun loginWanAndroid(
        @Field("username") username: String,
        @Field("password") password: String
    ): Observable<LoginResponse>

    /**
     * 注册
     * @param username username
     * @param password password
     * @param repassword repassword
     * @return Deferred<LoginResponse>
     */
    @POST(Constant.URI.REGISTER)
    @FormUrlEncoded
    fun registerWanAndroid(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") repassowrd: String
    ): Observable<LoginResponse>


    /**
     * 首页Banner
     * @return BannerResponse
     */
    @GET(Constant.URI.HOME_BANNER)
    fun getBanner(): Observable<BannerResponse>


}