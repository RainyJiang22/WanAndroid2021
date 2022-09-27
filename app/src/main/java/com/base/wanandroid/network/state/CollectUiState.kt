package com.base.wanandroid.network.state

/**
 * @author jiangshiyu
 * @date 2022/9/27
 * 收藏数据
 */
data class CollectUiState(
    //请求是否成功
    var isSuccess: Boolean = true,
    //收藏
    var collect: Boolean = false,
    //收藏Id
    var id: Int = -1,
    //请求失败的错误信息
    var errorMsg: String = ""
)