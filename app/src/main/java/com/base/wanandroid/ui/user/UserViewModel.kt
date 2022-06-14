package com.base.wanandroid.ui.user

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.base.wanandroid.bean.CoinInfoResponse
import com.base.wanandroid.bean.NoDataResponse
import com.base.wanandroid.bean.UserInfoResponse
import com.base.wanandroid.network.RetrofitHelper
import com.base.wanandroid.network.entity.ApiResponse
import com.base.wanandroid.ui.user.data.UserInfo
import io.reactivex.Observable

/**
 * @author jiangshiyu
 * @date 2022/6/10
 */
class UserViewModel(application: Application) : AndroidViewModel(application) {


    /**
     * 用户信息获取
     */
    fun getLoginUserInfo(username: String, password: String): Observable<UserInfo> {
        return login(username, password)
            .flatMap { user ->
                getCoinInfo().map { coin ->
                    UserInfo(user.data, coin.data)
                }
            }
    }

    /**
     * 用户注册信息获取
     */
    fun getRegisterUserInfo(
        username: String,
        password: String,
        rePassword: String
    ): Observable<UserInfo> {
        return register(username, password, rePassword)
            .flatMap { user ->
                getCoinInfo().map { coin ->
                    UserInfo(user.data, coin.data)
                }
            }
    }

    /**
     * 用户登录
     */
    private fun login(
        username: String,
        password: String
    ): Observable<ApiResponse<UserInfoResponse>> {
        return RetrofitHelper.get().loginWanAndroid(username, password)
    }


    /**
     * 个人积分查询
     */
    private fun getCoinInfo(): Observable<ApiResponse<CoinInfoResponse>> {
        return RetrofitHelper.get().getCoinInfo()
    }

    /**
     * 用户注册
     */
    private fun register(
        username: String,
        password: String,
        rePassword: String
    ): Observable<ApiResponse<UserInfoResponse>> {
        return RetrofitHelper.get().registerWanAndroid(username, password, rePassword)
    }

    /**
     * 退出
     */
    fun loginOut():Observable<NoDataResponse> {
        return RetrofitHelper.get().logout()
    }
}