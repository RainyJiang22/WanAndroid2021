package com.base.wanandroid.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.base.wanandroid.data.ArticleResponse
import com.base.wanandroid.data.ClassifyResponse
import com.base.wanandroid.network.apiService
import com.base.wanandroid.network.request.HttpRequestCoroutine
import com.base.wanandroid.network.state.ListDataUiState
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

/**
 * @author jiangshiyu
 * @date 2022/9/28
 */
open class RequestProjectViewModel : BaseViewModel() {

    var pageNo = 1

    var titleData: MutableLiveData<ResultState<ArrayList<ClassifyResponse>>> = MutableLiveData()

    var projectDataState: MutableLiveData<ListDataUiState<ArticleResponse>> = MutableLiveData()


    fun getProjectTitleData() {
        request({ apiService.getProjecTitle() }, titleData)
    }

    fun getProjectData(isRefresh: Boolean, cid: Int, isNew: Boolean = false) {
        if (isRefresh) {
            pageNo = if (isNew) 0 else 1
        }
        request({
            HttpRequestCoroutine.getProjectData(pageNo, cid, isNew)
        }, {
            //请求成功
            pageNo++
            val listDataUiState =
                ListDataUiState(
                    isSuccess = true,
                    isRefresh = isRefresh,
                    isEmpty = it.isEmpty(),
                    hasMore = it.hasMore(),
                    isFirstEmpty = isRefresh && it.isEmpty(),
                    listData = it.datas
                )
            projectDataState.value = listDataUiState
        }, {
            //请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<ArticleResponse>()
                )
            projectDataState.value = listDataUiState
        })
    }
}