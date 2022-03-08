package com.base.wanandroid.bean

import com.google.gson.annotations.SerializedName

/**
 * @author jiangshiyu
 * @date 2022/3/7
 */


data class ArticleListResponse(


    /**
     * apkLink :
     * author : 玉刚说
     * chapterId : 410
     * chapterName : 玉刚说
     * collect : false
     * courseId : 13
     * desc :
     * envelopePic :
     * fresh : false
     * id : 8367
     * link : https://mp.weixin.qq.com/s/uI7Fej1_qSJOJnzQ6offpw
     * niceDate : 2019-05-06
     * origin :
     * prefix :
     * projectLink :
     * publishTime : 1557072000000
     * superChapterId : 408
     * superChapterName : 公众号
     * tags : [{"name":"公众号","url":"/wxarticle/list/410/1"}]
     * title : 深扒 EventBus：register
     * type : 0
     * userId : -1
     * visible : 1
     * zan : 0
     */
    @SerializedName("baseResponse")
    val baseResponse: BaseResponse,
    @SerializedName("data")
    val data: Data
)
