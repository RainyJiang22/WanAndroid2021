package com.base.wanandroid.ui.platform

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.base.wanandroid.bean.ArticleListResponse
import com.base.wanandroid.bean.ClassificationListResponse
import com.base.wanandroid.network.RetrofitHelper
import io.reactivex.Observable

/**
 * @author jiangshiyu
 * @date 2022/5/31
 */
class PlatformViewModel(application: Application) : AndroidViewModel(application) {


    /**
     * 公众号列表数据
     */
    fun getPlatFormList(): Observable<ClassificationListResponse> {
        return RetrofitHelper.get().getPlatformList()
    }

    /**
     * 公众号历史数据
     */
    fun getPlatFormHistory(cid: Int, page: Int): Observable<ArticleListResponse> {
        return RetrofitHelper.get().getPlatformHistory(cid, page)
    }
}