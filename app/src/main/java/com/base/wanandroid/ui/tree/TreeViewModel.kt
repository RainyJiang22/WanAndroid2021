package com.base.wanandroid.ui.tree

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.base.wanandroid.bean.ArticleListResponse
import com.base.wanandroid.bean.TreeListResponse
import com.base.wanandroid.network.RetrofitHelper
import com.base.wanandroid.viewmodel.request.RequestSquareViewModel
import io.reactivex.Observable
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * @author jiangshiyu
 * @date 2022/6/2
 */
class TreeViewModel : RequestSquareViewModel() {


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