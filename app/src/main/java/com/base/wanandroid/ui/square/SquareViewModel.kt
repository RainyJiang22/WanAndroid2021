package com.base.wanandroid.ui.square

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.base.wanandroid.bean.ArticleListResponse
import com.base.wanandroid.network.RetrofitHelper
import com.base.wanandroid.viewmodel.request.RequestSquareViewModel
import io.reactivex.Observable
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * @author jiangshiyu
 * @date 2022/5/31
 */
class SquareViewModel : RequestSquareViewModel() {


    /**
     * 获取广场列表数据
     */
    fun getSquareList(page: Int): Observable<ArticleListResponse> {
        return RetrofitHelper.get().getSquareList(page)
    }
}