package com.base.wanandroid.ui.tree

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
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
class TreeActivity : BaseActivity<ActivityTreeBinding, TreeViewModel>() {


    /** Serialize界面传递参数: title */
    private val title: String by bundle()

    /** Serialize界面传递参数: content */
    private val content: List<String> by bundle()

    /** Serialize界面传递参数: cid */
    private val cid: List<Int> by bundle()

    /** Serialize界面传递参数: index 默认值0 */
    private val index: Int by bundle(0)

    private val fragments: ArrayList<Fragment> by lazy { arrayListOf() }


    override fun onBundle(bundle: Bundle) {
    }

    override fun init(savedInstanceState: Bundle?) {

        binding?.toolbar?.apply {
            title = this@TreeActivity.title
            //使用toolBar并使其外观与功能和actionBar一致
            setSupportActionBar(this)
            //使用默认导航按钮
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            //点击导航按钮关闭当前页面
            setNavigationOnClickListener { finish() }
            //拦截导航按钮长按吐司
            navigationContentDescription = ""
        }
        if (content.isEmpty()) return
        cid.forEach {
            fragments.add(TreeChildFragment.newInstance(it))
        }
        binding?.contentLayout?.let {
            it.viewPager.init(this@TreeActivity, fragments)
            it.magicIndicator.bindViewPager2(it.viewPager, content)
            it.viewPager.offscreenPageLimit = fragments.size
            it.viewPager.currentItem = index
        }


    }
}