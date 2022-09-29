package com.base.wanandroid.ui.tree

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.base.wanandroid.base.BaseActivity
import com.base.wanandroid.databinding.ActivityTreeBinding
import com.base.wanandroid.utils.bindViewPager2
import com.base.wanandroid.utils.init
import com.drake.serialize.intent.bundle

/**
 * @author jiangshiyu
 * @date 2022/6/8
 * 体系activity
 */
class TreeActivity : BaseActivity<TreeViewModel, ActivityTreeBinding>() {


    /** Serialize界面传递参数: title */
    private val title: String by bundle()

    /** Serialize界面传递参数: content */
    private val content: List<String> by bundle()

    /** Serialize界面传递参数: cid */
    private val cid: List<Int> by bundle()

    /** Serialize界面传递参数: index 默认值0 */
    private val index: Int by bundle(0)

    private val fragments: ArrayList<Fragment> by lazy { arrayListOf() }

    override fun initView(savedInstanceState: Bundle?) {
        if (content.isEmpty()) return
        cid.forEach {
            fragments.add(TreeChildFragment.newInstance(it))
        }
        mViewBind.contentLayout.let {
            it.viewPager.init(this@TreeActivity, fragments)
            it.magicIndicator.bindViewPager2(it.viewPager, content)
            it.viewPager.offscreenPageLimit = fragments.size
            it.viewPager.currentItem = index
        }
    }

}