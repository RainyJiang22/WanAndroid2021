package com.base.wanandroid.ui.platform

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.base.wanandroid.bean.ArticleListResponse
import com.base.wanandroid.bean.ClassificationListResponse
import com.base.wanandroid.network.RetrofitHelper
import com.base.wanandroid.viewmodel.request.RequestPlatformViewModel
import io.reactivex.Observable
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * @author jiangshiyu
 * @date 2022/5/31
 */
class PlatformViewModel : RequestPlatformViewModel() {


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