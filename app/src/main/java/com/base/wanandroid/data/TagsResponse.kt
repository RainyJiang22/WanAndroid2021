package com.base.wanandroid.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * @author jiangshiyu
 * @date 2022/9/23
 * 文章标签数据格式
 */
@Parcelize
data class TagsResponse(var name:String, var url:String): Parcelable
