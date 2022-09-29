package com.base.wanandroid.ui.user

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.base.wanandroid.bean.CoinInfoResponse
import com.base.wanandroid.bean.NoDataResponse
import com.base.wanandroid.bean.UserInfoResponse
import com.base.wanandroid.bean.base.BaseResponse
import com.base.wanandroid.network.RetrofitHelper
import com.base.wanandroid.network.entity.ApiResponse
import com.base.wanandroid.ui.user.data.UserInfo
import com.base.wanandroid.viewmodel.request.RequestLoginRegisterViewModel
import io.reactivex.Observable
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * @author jiangshiyu
 * @date 2022/6/10
 */
class UserViewModel : RequestLoginRegisterViewModel() {


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
     * 用户登录
     */
    private fun login(
        username: String,
        password: String
    ): Observable<BaseResponse<UserInfoResponse>> {
        return RetrofitHelper.get().loginWanAndroid(username, password)
    }


    /**
     * 个人积分查询
     */
    private fun getCoinInfo(): Observable<ApiResponse<CoinInfoResponse>> {
        return RetrofitHelper.get().getCoinInfo()
    }


    /**
     * 退出
     */
    fun loginOut(): Observable<NoDataResponse> {
        return RetrofitHelper.get().logout()
    }
}