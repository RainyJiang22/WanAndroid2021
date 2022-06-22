package com.base.wanandroid.utils

import com.base.wanandroid.BuildConfig
import com.base.wanandroid.application.WanAndroidApplication
import com.zchu.rxcache.RxCache
import com.zchu.rxcache.diskconverter.GsonDiskConverter
import java.io.File

/**
 * @author jiangshiyu
 * @date 2022/6/22
 */
object RxCacheManager {

    val rxCache: RxCache

    init {
        rxCache = RxCache.Builder()
            .appVersion(BuildConfig.VERSION_CODE)
            .diskDir(
                File(
                    WanAndroidApplication.getApplication()!!.getCacheDir()
                        .getPath() + File.separator + "widget_cache"
                )
            )
            .diskConverter(GsonDiskConverter())
            .memoryMax(2 * 1024 * 1024)
            .diskMax(20 * 1024 * 1024.toLong())
            .build()
    }
}