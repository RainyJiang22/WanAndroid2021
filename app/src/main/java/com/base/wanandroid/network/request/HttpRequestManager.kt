package com.base.wanandroid.network.request

import com.base.wanandroid.data.ApiPagerResponse
import com.base.wanandroid.data.ApiResponse
import com.base.wanandroid.data.ArticleResponse
import com.base.wanandroid.network.apiService
import com.base.wanandroid.utils.CacheUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

/**
 * @author jiangshiyu
 * @date 2022/9/27
 * 处理协程的请求类
 */

val HttpRequestCoroutine: HttpRequestManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    HttpRequestManager()
}


class HttpRequestManager {

    /**
     * 获取首页文章数据
     */
    suspend fun getHomeData(pageNo: Int): ApiResponse<ApiPagerResponse<ArrayList<ArticleResponse>>> {
        return withContext(Dispatchers.IO) {
            val listData = async { apiService.getAritrilList(pageNo) }
            //如果App配置打开了首页请求置顶文章，且是第一页
            if (CacheUtil.isNeedTop() == true && pageNo == 0) {
                val topData = async { apiService.getTopAritrilList() }
                listData.await().data.datas.addAll(0, topData.await().data)
                listData.await()
            } else {
                listData.await()
            }
        }
    }



}