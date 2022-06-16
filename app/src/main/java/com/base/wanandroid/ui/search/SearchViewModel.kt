package com.base.wanandroid.ui.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.base.wanandroid.bean.ArticleListResponse
import com.base.wanandroid.bean.HomeListResponse
import com.base.wanandroid.bean.HotKeyResponse
import com.base.wanandroid.network.RetrofitHelper
import io.reactivex.Observable

/**
 * @author jiangshiyu
 * @date 2022/6/16
 */
class SearchViewModel(application: Application) : AndroidViewModel(application) {


    /**
     * 搜索热词
     */
    fun getHotKeyList(): Observable<HotKeyResponse> {
        return RetrofitHelper.get().getHotKeyList()
    }


    /**
     * 搜索结果
     */
    fun getSearchResult(page: Int, key: String): Observable<ArticleListResponse> {
        return RetrofitHelper.get().getSearchList(page, key)
    }
}