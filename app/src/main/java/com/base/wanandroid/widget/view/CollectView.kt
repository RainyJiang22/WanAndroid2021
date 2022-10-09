package com.base.wanandroid.widget.view

import android.content.Context
import android.util.AttributeSet
import com.base.wanandroid.R
import com.base.wanandroid.utils.AppConfig
import com.base.wanandroid.utils.CacheUtil
import com.blankj.utilcode.util.ToastUtils
import per.goweii.reveallayout.RevealLayout

/**
 * @author jiangshiyu
 * @date 2022/6/14
 * 收藏视图
 */
class CollectView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    RevealLayout(context, attrs, defStyleAttr) {

    private lateinit var mOnClickListener: OnClickListener

    override fun initAttr(attrs: AttributeSet) {
        super.initAttr(attrs)
        setCheckWithExpand(true)
        setUncheckWithExpand(false)
        setAnimDuration(400)
        setAllowRevert(false)
    }

    override fun getCheckedLayoutId() = R.layout.view_reveal_like_checked

    override fun getUncheckedLayoutId() = R.layout.view_reveal_like_unchecked

    fun setOnClickListener(onClickListener: OnClickListener) {
        mOnClickListener = onClickListener
        setOnClickListener {
            if (CacheUtil.isLogin()) {
                //登陆过直接走点击事件回调
                mOnClickListener.onClick(this@CollectView)
            } else {
                //否则弹吐司并且不给选中
                ToastUtils.showShort(R.string.please_login)
                isChecked = false
            }
        }
    }

    interface OnClickListener {
        fun onClick(v: CollectView)
    }
}