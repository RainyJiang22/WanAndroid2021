package com.base.wanandroid.ui.collect

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.base.wanandroid.bean.ArticleListResponse
import com.base.wanandroid.bean.BannerResponse
import com.base.wanandroid.bean.ClassificationListResponse
import com.base.wanandroid.bean.NoDataResponse
import com.base.wanandroid.bean.ShareListResponse
import com.base.wanandroid.bean.TreeListResponse
import com.base.wanandroid.network.RetrofitHelper
import com.base.wanandroid.utils.RxCacheManager
import com.base.wanandroid.utils.TimeConstant
import com.zchu.rxcache.data.CacheResult
import com.zchu.rxcache.kotlin.rxCache
import com.zchu.rxcache.stategy.CacheStrategy
import io.reactivex.Observable
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * @author jiangshiyu
 * @date 2022/6/15
 */
class ArticleViewModel : BaseViewModel() {


    /**
     * 搜索结果
     */
    fun getSearchResult(page: Int, key: String): Observable<ArticleListResponse> {
        return RetrofitHelper.get().getSearchList(page, key)
    }
}