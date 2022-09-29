package com.base.wanandroid.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.base.wanandroid.data.ArticleResponse
import com.base.wanandroid.data.ClassifyResponse
import com.base.wanandroid.network.apiService
import com.base.wanandroid.network.state.ListDataUiState
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

/**
 * @author jiangshiyu
 * @date 2022/9/29
 */
open class RequestPlatformViewModel : BaseViewModel() {

    var pageNo = 1

    var classifyData: MutableLiveData<ResultState<ArrayList<ClassifyResponse>>> = MutableLiveData()

    var platformDataState: MutableLiveData<ListDataUiState<ArticleResponse>> = MutableLiveData()


    fun getPlatformTitleData() {
        request({ apiService.getPublicTitle() }, classifyData)
    }

    fun getPlatformData(isRefresh: Boolean, cid: Int) {
        if (isRefresh) {
            pageNo = 1
        }

        request({ apiService.getPublicData(pageNo, cid) }, {
            //请求成功
            val listDataUiState =
                ListDataUiState(
                    isSuccess = true,
                    isRefresh = isRefresh,
                    isEmpty = it.isEmpty(),
                    hasMore = it.hasMore(),
                    isFirstEmpty = isRefresh && it.isEmpty(),
                    listData = it.datas
                )
            platformDataState.value = listDataUiState
        }, {
            //请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<ArticleResponse>()
                )
            platformDataState.value = listDataUiState
        })
    }
}