package com.base.wanandroid.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.base.wanandroid.data.CollectResponse
import com.base.wanandroid.data.CollectUrlResponse
import com.base.wanandroid.network.apiService
import com.base.wanandroid.network.state.CollectUiState
import com.base.wanandroid.network.state.ListDataUiState
import com.huantansheng.easyphotos.models.puzzle.Line
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request

/**
 * @author jiangshiyu
 * @date 2022/10/3
 */
open class RequestCollectViewModel : BaseViewModel() {


    private var pageNo = 0


    //收藏文章
    val collectUiState: MutableLiveData<CollectUiState> = MutableLiveData()

    //收藏网址
    val collectUrlUiState: MutableLiveData<CollectUiState> = MutableLiveData()

    //收藏de文章数据
    var articleDataState: MutableLiveData<ListDataUiState<CollectResponse>> = MutableLiveData()

    //收藏de网址数据
    var urlDataState: MutableLiveData<ListDataUiState<CollectUrlResponse>> = MutableLiveData()


    /**
     * 收藏文章
     */
    fun collect(id: Int) {
        request({ apiService.collect(id) }, {
            val uiState = CollectUiState(isSuccess = true, collect = true, id = id)
            collectUiState.value = uiState
        }, {
            val uiState =
                CollectUiState(isSuccess = false, collect = false, errorMsg = it.errorMsg, id = id)
            collectUiState.value = uiState
        })
    }


    /**
     * 取消收藏文章
     */
    fun unCollect(id: Int) {
        request({ apiService.uncollect(id) }, {
            val uiState = CollectUiState(isSuccess = true, collect = false, id = id)
            collectUiState.value = uiState
        }, {
            val uiState =
                CollectUiState(isSuccess = false, collect = true, errorMsg = it.errorMsg, id = id)
            collectUiState.value = uiState
        })
    }


    /**
     * 收藏网址
     */
    fun collectUrl(name: String, link: String) {
        request({ apiService.collectUrl(name, link) }, {
            val uiState = CollectUiState(isSuccess = true, collect = true, id = it.id)
            collectUrlUiState.value = uiState
        }, {
            val uiState =
                CollectUiState(isSuccess = false, collect = false, errorMsg = it.errorMsg, id = 0)
            collectUrlUiState.value = uiState
        })
    }


    /**
     * 取消收藏网址
     */
    fun unCollectUrl(id: Int) {
        request({ apiService.uncollect(id) }, {
            val uiState = CollectUiState(isSuccess = true, collect = false, id = id)
            collectUrlUiState.value = uiState
        }, {
            val uiState =
                CollectUiState(isSuccess = false, collect = true, errorMsg = it.errorMsg, id = id)
            collectUrlUiState.value = uiState
        })
    }


    /**
     * 获取收藏文章列表
     */
    fun getCollectArticleData(isRefresh: Boolean) {
        if (isRefresh) {
            pageNo = 0
        }
        request({ apiService.getCollectData(pageNo) }, {
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
            articleDataState.value = listDataUiState
        }, {
            //请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<CollectResponse>()
                )
            articleDataState.value = listDataUiState
        })
    }

    /**
     * 获取文章收藏网址
     */
    fun getCollectUrlData() {
        request({ apiService.getCollectUrlData() }, {
            //请求成功
            it.map {
                if (it.order == 0) {
                    it.order = 1
                }
            }
            val listDataUiState =
                ListDataUiState(
                    isRefresh = true,
                    isSuccess = true,
                    hasMore = false,
                    isEmpty = it.isEmpty(),
                    listData = it
                )
            urlDataState.value = listDataUiState
        }, {
            //请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    listData = arrayListOf<CollectUrlResponse>()
                )
            urlDataState.value = listDataUiState
        })
    }

}