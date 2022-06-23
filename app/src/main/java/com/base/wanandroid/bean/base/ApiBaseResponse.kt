package com.base.wanandroid.bean.base

/**
 * @author jiangshiyu
 * @date 2022/6/23
 */
data class ApiBaseResponse<T>(val errorCode: Int, val errorMsg: String, val data: T)