package com.base.wanandroid.ui.setting

import android.os.Bundle
import com.base.wanandroid.base.SimpleDialogFragment
import com.base.wanandroid.base.WindowParam
import com.base.wanandroid.databinding.FragmentAboutMeBinding

/**
 * @author jiangshiyu
 * @date 2022/6/9
 */
@WindowParam(outSideCanceled = true, canceled = true)
class AboutMeFragment : SimpleDialogFragment<FragmentAboutMeBinding>() {

    override fun build(savedInstanceState: Bundle?) {

        onView {

        }
    }
}