package com.base.wanandroid.ui.project

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.base.wanandroid.bean.ArticleListResponse
import com.base.wanandroid.bean.ClassificationListResponse
import com.base.wanandroid.network.RetrofitHelper
import com.base.wanandroid.viewmodel.request.RequestProjectViewModel
import io.reactivex.Observable
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * @author jiangshiyu
 * @date 2022/3/8
 */
class ProjectViewModel : RequestProjectViewModel() {


    /**
     * 获取项目分类数据
     */
    fun getProjectType(): Observable<ClassificationListResponse> {
        return RetrofitHelper.get().getProjectType()
    }

    /**
     * 获取项目列表数据
     */
    fun getProjectList(page: Int, cid: Int): Observable<ArticleListResponse> {
        return RetrofitHelper.get().getProjectList(page, cid)
    }

    /**
     * 获取项目最新数据
     */
    fun getNewProject(page: Int): Observable<ArticleListResponse> {
        return RetrofitHelper.get().getProjectNew(page)
    }
}