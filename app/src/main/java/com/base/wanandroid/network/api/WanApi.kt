package com.base.wanandroid.network.api

import com.base.wanandroid.bean.ArticleListResponse
import com.base.wanandroid.bean.BannerResponse
import com.base.wanandroid.bean.ClassificationListResponse
import com.base.wanandroid.bean.CoinInfoListResponse
import com.base.wanandroid.bean.CoinInfoResponse
import com.base.wanandroid.bean.CollectListResponse
import com.base.wanandroid.bean.CollectResponse
import com.base.wanandroid.bean.Data
import com.base.wanandroid.bean.FriendListResponse
import com.base.wanandroid.bean.HomeListResponse
import com.base.wanandroid.bean.HotKeyResponse
import com.base.wanandroid.bean.IntegralListResponse
import com.base.wanandroid.bean.LoginResponse
import com.base.wanandroid.bean.NavigationListResponse
import com.base.wanandroid.bean.NoDataResponse
import com.base.wanandroid.bean.SearchHotResponse
import com.base.wanandroid.bean.ShareListResponse
import com.base.wanandroid.bean.ShareResponse
import com.base.wanandroid.bean.TreeListResponse
import com.base.wanandroid.bean.UserInfoResponse
import com.base.wanandroid.bean.base.ApiPagerResponse
import com.base.wanandroid.bean.base.BaseResponse
import com.base.wanandroid.constant.Constant
import com.base.wanandroid.network.entity.ApiResponse
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
    fun getHomeList(@Path("page") page: Int): Observable<ApiResponse<Data>>


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
    ): Observable<ArticleListResponse>

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
    ): Observable<BaseResponse<UserInfoResponse>>

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
    ): Observable<BaseResponse<UserInfoResponse>>


    /**
     * 获取个人积分，需要用户登录
     */
    @GET(Constant.URI.CoinInfo)
    fun getCoinInfo(): Observable<ApiResponse<CoinInfoResponse>>

    /**
     * 首页Banner
     * @return BannerResponse
     */
    @GET(Constant.URI.HOME_BANNER)
    fun getBanner(): Observable<BannerResponse>


    /**
     * 项目分类数据
     */
    @GET(Constant.URI.TYPE_PROJECT)
    fun getProjectType(): Observable<ClassificationListResponse>


    /**
     * 最新项目数据
     * https://www.wanandroid.com/article/listproject/{page}/json
     */
    @GET(Constant.URI.Project_NEW)
    fun getProjectNew(@Path("page") page: Int): Observable<ArticleListResponse>


    /**
     * 项目列表数据
     * https://www.wanandroid.com/project/list/{page}/json?cid =111
     */
    @GET(Constant.URI.PROJECT_LIST)
    fun getProjectList(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): Observable<ArticleListResponse>


    /**
     * 广场列表数据
     */
    @GET(Constant.URI.SQUARE_ARTICLE)
    fun getSquareList(@Path("page") page: Int): Observable<ArticleListResponse>


    /**
     *公众号列表数据
     */
    @GET(Constant.URI.PLATFORM_LIST)
    fun getPlatformList(): Observable<ClassificationListResponse>


    /**
     * 公众号历史数据
     */
    @GET(Constant.URI.PLATFORM_HISTORY)
    fun getPlatformHistory(
        @Path("cid") cid: Int,
        @Path("page") page: Int
    ): Observable<ArticleListResponse>


    /**
     * 问答列表
     */
    @GET(Constant.URI.InquiryAnswerAPI)
    fun getAnswerList(@Path("page") page: Int): Observable<ArticleListResponse>


    /**
     * 导航tab
     */
    @GET(Constant.URI.NAVI)
    fun getNaviList(): Observable<NavigationListResponse>


    /**
     * 退出登录
     */
    @GET(Constant.URI.LOGOUT)
    fun logout(): Observable<NoDataResponse>


    /**
     * 收藏列表展示
     */
    @GET(Constant.URI.COLLECT_LIST)
    fun getCollectList(@Path("page") page: Int): Observable<ApiPagerResponse<CollectResponse>>


    /**
     * 收藏当前文章
     */
    @POST(Constant.URI.COLLECT_INNER_ARTICLE)
    fun collectCurrentArticle(@Path("articleId") articleId: Int): Observable<NoDataResponse>


    /**
     * 取消收藏当前文章
     */
    @POST(Constant.URI.UN_COLLECT)
    @FormUrlEncoded
    fun unCollectArticle(
        @Path("id") id: Int,
        @Field("originId") originId: Int = -1
    ): Observable<NoDataResponse>


    /**
     * 收藏界面取消收藏文章
     */
    @POST(Constant.URI.UserUnCollectArticleAPI)
    @FormUrlEncoded
    fun userUnCollectArticle(
        @Path("id") id: Int,
        @Field("originId") originId: Int = -1
    ): Observable<NoDataResponse>


    /**
     * 我的积分列表
     */
    @GET(Constant.URI.IntegralListAPI)
    fun integralList(@Path("page") page: Int): Observable<IntegralListResponse>


    /**
     * 积分排行列表
     */
    @GET(Constant.URI.LeaderboardAPI)
    fun leaderboardList(@Path("page") page: Int): Observable<CoinInfoListResponse>


    /**
     * 分享列表
     */
    @GET(Constant.URI.ShareListAPI)
    fun shareList(@Path("page") page: Int): Observable<ShareListResponse>


    /**
     *删除对应分享文章
     */
    @POST(Constant.URI.DeleteShareAPI)
    fun deleteShareArticle(@Path("id") id: Int): Observable<NoDataResponse>

    /**
     * 分享文章
     */
    @POST(Constant.URI.ShareArticleAPI)
    @FormUrlEncoded
    fun shareArticle(
        @Field("title") title: String,
        @Field("link") link: String
    ): Observable<NoDataResponse>
}