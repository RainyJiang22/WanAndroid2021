package com.base.wanandroid.base

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import me.hgj.jetpackmvvm.base.activity.BaseVmVbActivity
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * @author jiangshiyu
 * @date 2022/9/21
 */
abstract class BaseActivity<VM : BaseViewModel, V : ViewBinding> : BaseVmVbActivity<VM,V>() {
    override fun createObserver() {
    }

    override fun dismissLoading() {
    }

    override fun initView(savedInstanceState: Bundle?) {
    }

    override fun showLoading(message: String) {

    }


}