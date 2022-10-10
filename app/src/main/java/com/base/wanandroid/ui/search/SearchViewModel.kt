package com.base.wanandroid.ui.search

import com.base.wanandroid.bean.ArticleListResponse
import com.base.wanandroid.bean.HotKeyResponse
import com.base.wanandroid.network.RetrofitHelper
import io.reactivex.Observable
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * @author jiangshiyu
 * @date 2022/6/16
 */
class SearchViewModel : BaseViewModel() {


    /**
     * 搜索热词
     */
    fun getHotKeyList(): Observable<HotKeyResponse> {
        return RetrofitHelper.get().getHotKeyList()
    }

}