package com.base.wanandroid.ui.platform

import android.os.Bundle
import com.base.wanandroid.base.BaseFragment
import com.base.wanandroid.databinding.FragmentChildBinding

/**
 * @author jiangshiyu
 * @date 2022/5/31
 */
class PlatformChildFragment : BaseFragment<FragmentChildBinding, PlatformViewModel>() {


    companion object {

        fun newInstance(cid: Int): PlatformChildFragment {
            val args = Bundle().apply {
                putInt("cid", cid)
            }
            val fragment = PlatformChildFragment()
            fragment.arguments = args
            return fragment
        }

    }

    override fun onBundle(bundle: Bundle) {
    }

    override fun init(savedInstanceState: Bundle?) {

    }
}