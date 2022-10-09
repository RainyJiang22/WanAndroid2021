package com.base.wanandroid.ui.mine

import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.base.wanandroid.BuildConfig
import com.base.wanandroid.R
import com.base.wanandroid.application.appViewModel
import com.base.wanandroid.base.BaseFragment
import com.base.wanandroid.data.IntegralResponse
import com.base.wanandroid.databinding.FragmentMineBinding
import com.base.wanandroid.ext.init
import com.base.wanandroid.network.NetworkApi
import com.base.wanandroid.ui.collect.CollectActivity
import com.base.wanandroid.ui.history.HistoryRecordActivity
import com.base.wanandroid.ui.integral.IntegralActivity
import com.base.wanandroid.ui.integral.LeaderBoardActivity
import com.base.wanandroid.ui.setting.SettingActivity
import com.base.wanandroid.ui.share.OnShareListener
import com.base.wanandroid.ui.share.ShareFragment
import com.base.wanandroid.ui.user.LoginActivity
import com.base.wanandroid.ui.user.UserViewModel
import com.base.wanandroid.utils.AppConfig
import com.base.wanandroid.utils.CacheUtil
import com.base.wanandroid.utils.closeFragment
import com.base.wanandroid.utils.showFragment
import com.base.wanandroid.viewmodel.request.RequestMineViewModel
import com.base.wanandroid.widget.Dialog
import com.blankj.utilcode.util.ToastUtils
import com.drake.serialize.intent.openActivity
import me.hgj.jetpackmvvm.ext.parseState
import me.hgj.jetpackmvvm.ext.util.notNull

/**
 * @author jiangshiyu
 * @date 2022/5/31
 * 我的界面
 */
class MineFragment : BaseFragment<UserViewModel, FragmentMineBinding>() {


    private val requestMeViewModel: RequestMineViewModel by viewModels()

    private var rank: IntegralResponse? = null


    override fun initView(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)

        mViewBind.apply {
            appViewModel.userInfo.value?.let {
                appViewModel.userInfo.value?.let { mViewModel.name.set(it.nickname.ifEmpty { it.username }) }
            }

            if (!CacheUtil.isLogin()) {
                headerImage.setOnClickListener {
                    openActivity<LoginActivity>()
                }
            }

            rankImage.setOnClickListener {
                //积分排行页
                openActivity<LeaderBoardActivity>()
            }
            mineIntegral.setOnClickListener {
                if (CacheUtil.isLogin()) {
                    //我的积分页
                    openActivity<IntegralActivity>()
                } else {
                    ToastUtils.showShort(getString(R.string.please_login))
                    openActivity<LoginActivity>()
                }
            }
            mineCollect.setOnClickListener {
                if (CacheUtil.isLogin()) {
                    //收藏界面
                    openActivity<CollectActivity>()
                } else {
                    ToastUtils.showShort(getString(R.string.please_login))
                    openActivity<LoginActivity>()
                }
            }
            mineShare.setOnClickListener {
                if (CacheUtil.isLogin()) {
                    //分享
                    val shareFragment = ShareFragment()
                    shareFragment.onShareListener = object : OnShareListener {
                        override fun removeFragment() {
                            closeFragment(shareFragment)
                        }
                    }
                    showFragment(shareFragment, R.id.frame_share)
                } else {
                    ToastUtils.showShort(getString(R.string.please_login))
                    openActivity<LoginActivity>()
                }
            }
            mineRecord.setOnClickListener { openActivity<HistoryRecordActivity>() }
            mineSetting.setOnClickListener { openActivity<SettingActivity>() }
            //未登录隐藏登出项，登陆可见
            mineExit.isVisible = !CacheUtil.isLogin()


            mineExit.setOnClickListener {
                //退出登录
                Dialog.getConfirmDialog(
                    requireContext(),
                    getString(R.string.exit_confirm)
                ) { _, _ ->
                    //清空cookie
                    NetworkApi.INSTANCE.cookieJar.clear()
                    CacheUtil.setUser(null)
                    appViewModel.userInfo.value = null
                }.show()
            }
        }

        mViewBind.swipeRefresh.init {
            mViewBind.swipeRefresh.isRefreshing = true
            requestMeViewModel.getIntegral()
        }
    }

    override fun lazyLoadData() {
        appViewModel.userInfo.value?.run {
            mViewBind.swipeRefresh.isRefreshing = true
            requestMeViewModel.getIntegral()
        }
    }

    override fun createObserver() {
        requestMeViewModel.mineData.observe(viewLifecycleOwner) { resultState ->
            mViewBind.swipeRefresh.isRefreshing = false
            parseState(resultState, {
                rank = it
                mViewModel.info.set("id：${it.userId}　排名：${it.rank}")
                mViewModel.integral.set(it.coinCount)
                mViewBind.apply {
                    meInfo.text = "id：${it.userId}　排名：${it.rank}"
                    mineIntegral.setRightText(it.coinCount.toString())
                    AppConfig.CoinCount = it.coinCount.toString()
                }
            }, {
                ToastUtils.showShort(it.errorMsg)
            })

        }


        appViewModel.run {
            userInfo.observeInFragment(this@MineFragment) {
                if (BuildConfig.LOG_ENABLE) {
                    Log.d("mine", "createObserver: is Login")
                }
                it.notNull({
                    mViewBind.swipeRefresh.isRefreshing = true
                    mViewModel.name.set(it.nickname.ifEmpty { it.username })
                    mViewBind.apply {
                        //用户名
                        userText.text = it.nickname.ifEmpty { it.username }
                    }
                    requestMeViewModel.getIntegral()
                }, {
                    mViewModel.name.set("请先登录~")
                    mViewModel.info.set("id：--　排名：--")
                    mViewModel.integral.set(0)
                    mViewBind.apply {
                        meInfo.text = "id：--　排名：--"
                        mineIntegral.setRightText("0")
                        userText.text = getString(R.string.my_user)
                    }
                })
            }
        }
    }

}