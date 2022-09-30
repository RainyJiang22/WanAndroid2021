package com.base.wanandroid.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.base.wanandroid.data.UserInfo
import com.base.wanandroid.network.apiService
import com.base.wanandroid.network.request.HttpRequestCoroutine
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

/**
 * @author jiangshiyu
 * @date 2022/9/29
 * 登录注册viewModel
 */
open class RequestLoginRegisterViewModel : BaseViewModel() {


    var loginResult = MutableLiveData<ResultState<UserInfo>>()


    var registerResult = MutableLiveData<ResultState<Any>>()

    /**
     * 登录
     */
    fun loginReq(userName: String, password: String) {
        request({ apiService.login(userName, password) }, loginResult)
    }


    /**
     * 注册并且登录
     */
    fun registerAndLogin(userName: String, password: String) {
        request({ HttpRequestCoroutine.register(userName, password) }, loginResult)
    }

    /**
     * 单纯的注册
     */
    fun register(userName: String, password: String, rePassword: String) {
        request({ apiService.register(userName, password, rePassword) }, registerResult)
    }


}