package com.base.wanandroid.ui.collect

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.base.wanandroid.R
import com.base.wanandroid.base.BaseActivity
import com.base.wanandroid.databinding.ActivityCollectBinding
import com.base.wanandroid.utils.bindViewPager2
import com.base.wanandroid.utils.init
import com.base.wanandroid.utils.initFloatBtn
import com.drake.brv.PageRefreshLayout
import com.drake.net.Get
import com.drake.net.utils.scope
import com.kingja.loadsir.core.LoadService

/**
 * @author jiangshiyu
 * @date 2022/6/14
 * 收藏界面
 */
class CollectActivity : BaseActivity<CollectViewModel, ActivityCollectBinding>() {


    var titleData = arrayListOf("文章", "网址")


    private var fragments: ArrayList<Fragment> = arrayListOf()

    init {
        fragments.add(CollectArticleFragment())
        fragments.add(CollectUrlFragment())
    }

    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.apply {
            collectViewPager.init(this@CollectActivity, fragments)
            collectMagicIndicator.bindViewPager2(collectViewPager, mStringList = titleData)
            titleBar.leftView.setOnClickListener {
                finishAfterTransition()
            }
        }
    }

}