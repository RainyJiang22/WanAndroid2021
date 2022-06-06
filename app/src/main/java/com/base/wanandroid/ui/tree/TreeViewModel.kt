package com.base.wanandroid.ui.tree

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.base.wanandroid.bean.ArticleListResponse
import com.base.wanandroid.bean.TreeListResponse
import com.base.wanandroid.network.RetrofitHelper
import com.base.wanandroid.network.entity.ApiResponse
import com.example.wanAndroid.logic.model.SystemResponse
import io.reactivex.Observable

/**
 * @author jiangshiyu
 * @date 2022/6/2
 */
class TreeViewModel(application: Application) : AndroidViewModel(application) {


    /**
     * 体系列表
     */
    fun getTreeList(): Observable<TreeListResponse> {
        return RetrofitHelper.get().getTypeTreeList()
    }


    /**
     * 知识体系下的文章
     */
    fun getTreeArticleList(cid: Int, page: Int): Observable<ArticleListResponse> {
        return RetrofitHelper.get().getTreeArticleList(page, cid)
    }
}