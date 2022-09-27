package com.base.wanandroid.widget.loadcallback

import com.base.wanandroid.R
import com.kingja.loadsir.callback.Callback
/**
 * @author jiangshiyu
 * @date 2022/9/27
 */
class LoadingCallBack : Callback() {
    override fun onCreateView(): Int {
        return R.layout.layout_loading
    }
}