package com.base.wanandroid.ui.user

import com.base.wanandroid.utils.ColorUtil
import com.base.wanandroid.viewmodel.request.RequestLoginRegisterViewModel
import me.hgj.jetpackmvvm.callback.databind.IntObservableField
import me.hgj.jetpackmvvm.callback.databind.StringObservableField

/**
 * @author jiangshiyu
 * @date 2022/6/10
 */
class UserViewModel : RequestLoginRegisterViewModel() {


    var name = StringObservableField("请先登录~")

    var integral = IntObservableField(0)

    var info = StringObservableField("id：--　排名：-")

    var imageUrl = StringObservableField(ColorUtil.randomImage())

}