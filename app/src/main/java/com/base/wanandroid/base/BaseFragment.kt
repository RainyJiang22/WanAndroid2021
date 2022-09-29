package com.base.wanandroid.base

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import me.hgj.jetpackmvvm.base.fragment.BaseVmVbFragment
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * @author jiangshiyu
 * @date 2022/9/21
 */
abstract class BaseFragment<VM : BaseViewModel, V : ViewBinding> : BaseVmVbFragment<VM, V>() {
    override fun createObserver() {
    }

    override fun dismissLoading() {
    }

    override fun initView(savedInstanceState: Bundle?) {
    }

    override fun lazyLoadData() {
        //懒加载数据
    }

    override fun showLoading(message: String) {
    }
}