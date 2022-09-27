package com.base.wanandroid.viewmodel.request

import androidx.lifecycle.MutableLiveData
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.state.ResultState

/**
 * @author jiangshiyu
 * @date 2022/9/27
 */
class RequestArticleViewModel : BaseViewModel() {


    var pageNo = 0

    var addData = MutableLiveData<ResultState<Any?>>()




}