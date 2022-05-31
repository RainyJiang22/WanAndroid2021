package com.base.wanandroid.ui.square

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.base.wanandroid.bean.ArticleListResponse
import com.base.wanandroid.network.RetrofitHelper
import io.reactivex.Observable

/**
 * @author jiangshiyu
 * @date 2022/5/31
 */
class SquareViewModel(application: Application) : AndroidViewModel(application) {


    /**
     * 获取广场列表数据
     */
    fun getSquareList(page: Int): Observable<ArticleListResponse> {
        return RetrofitHelper.get().getSquareList(page)
    }
}