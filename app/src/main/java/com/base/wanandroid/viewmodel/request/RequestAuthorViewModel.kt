package com.base.wanandroid.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.base.wanandroid.data.ArticleResponse
import com.base.wanandroid.data.ShareResponse
import com.base.wanandroid.network.apiService
import com.base.wanandroid.network.state.ListDataUiState
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request

/**
 * @author jiangshiyu
 * @date 2022/10/11
 */
open class RequestAuthorViewModel : BaseViewModel() {


    var pageNo = 1

    var shareListDataUiState = MutableLiveData<ListDataUiState<ArticleResponse>>()

    var shareResponse = MutableLiveData<ShareResponse>()

    fun getLookInfo(id: Int, isRefresh: Boolean) {
        if (isRefresh) {
            pageNo = 1
            request({ apiService.getShareByidData(id, pageNo) }, {
                //请求成功
                pageNo++
                shareResponse.value = it
                val listDataUiState =
                    ListDataUiState(
                        isSuccess = true,
                        isRefresh = it.shareArticles.isRefresh(),
                        isEmpty = it.shareArticles.isEmpty(),
                        hasMore = it.shareArticles.hasMore(),
                        isFirstEmpty = isRefresh && it.shareArticles.isEmpty(),
                        listData = it.shareArticles.datas
                    )
                shareListDataUiState.value = listDataUiState
            }, {
                //请求失败
                val listDataUiState =
                    ListDataUiState(
                        isSuccess = false,
                        errMessage = it.errorMsg,
                        isRefresh = isRefresh,
                        listData = arrayListOf<ArticleResponse>()
                    )
                shareListDataUiState.value = listDataUiState
            })
        }
    }
}