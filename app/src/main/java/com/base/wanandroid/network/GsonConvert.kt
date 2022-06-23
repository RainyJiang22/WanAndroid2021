package com.base.wanandroid.network

import com.drake.net.convert.JSONConvert
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.lang.reflect.Type

class GsonConvert : JSONConvert(code = "errorCode", message = "errorMsg", success = "0") {
    private val gson: Gson = GsonBuilder().serializeNulls().create()

    override fun <S> String.parseBody(succeed: Type): S? {
        return gson.fromJson(this, succeed)
    }
}