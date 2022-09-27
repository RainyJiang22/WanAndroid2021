package com.base.wanandroid.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.base.wanandroid.data.ArticleResponse
import com.base.wanandroid.data.BannerResponse
import com.base.wanandroid.network.apiService
import com.base.wanandroid.network.request.HttpRequestCoroutine
import com.base.wanandroid.network.state.ListDataUiState
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

/**
 * @author jiangshiyu
 * @date 2022/9/27
 */
class RequestHomeViewModel : BaseViewModel() {

    //页码
    var pageNo = 0

    //首页文章列表数据
    var homeDataState: MutableLiveData<ListDataUiState<ArticleResponse>> = MutableLiveData()

    //首页轮播图数据
    var bannerData: MutableLiveData<ResultState<ArrayList<BannerResponse>>> = MutableLiveData()


    /**
     * 获取首页文章列表数据
     * @param isRefresh 是否刷新 第一页
     */
    fun getHomeData(isRefresh: Boolean) {
        if (isRefresh) {
            pageNo = 0
        }
        request({ HttpRequestCoroutine.getHomeData(pageNo) }, {
            //请求成功
            pageNo++
            val listDataUiState = ListDataUiState(
                isSuccess = true,
                isRefresh = isRefresh,
                isEmpty = it.isEmpty(),
                hasMore = it.hasMore(),
                isFirstEmpty = isRefresh && it.isEmpty(),
                listData = it.datas
            )
            homeDataState.value = listDataUiState
        }, {
            //请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<ArticleResponse>()
                )
            homeDataState.value = listDataUiState
        })
    }

    /**
     * 获取轮播图数据
     */
    fun getBannerData() {
        request({ apiService.getBanner() }, bannerData)
    }


}