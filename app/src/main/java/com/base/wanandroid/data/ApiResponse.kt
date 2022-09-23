package com.base.wanandroid.data

import me.hgj.jetpackmvvm.network.BaseResponse

/**
 * @author jiangshiyu
 * @date 2022/9/23
 * 服务器返回的基类
 */
data class ApiResponse<T>(val errorCode: Int, val errorMsg: String, val data: T) :
    BaseResponse<T>() {

    //错误码为 0 就代表请求成功，根据自己的业务需求来改变,
    override fun getResponseCode(): Int = errorCode

    override fun getResponseData() = data

    override fun getResponseMsg(): String = errorMsg

    override fun isSucces(): Boolean = errorCode == 0

}