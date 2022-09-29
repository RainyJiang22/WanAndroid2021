package com.base.wanandroid.ui.mine

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.base.wanandroid.R
import com.base.wanandroid.base.BaseFragment
import com.base.wanandroid.databinding.FragmentMineBinding
import com.base.wanandroid.ui.collect.CollectActivity
import com.base.wanandroid.ui.history.HistoryRecordActivity
import com.base.wanandroid.ui.integral.IntegralActivity
import com.base.wanandroid.ui.integral.LeaderBoardActivity
import com.base.wanandroid.ui.setting.SettingActivity
import com.base.wanandroid.ui.share.ShareActivity
import com.base.wanandroid.ui.user.LoginActivity
import com.base.wanandroid.ui.user.UserViewModel
import com.base.wanandroid.ui.user.data.UserInfo
import com.base.wanandroid.utils.AppConfig
import com.base.wanandroid.utils.CacheUtil
import com.base.wanandroid.utils.RxTransformer
import com.base.wanandroid.utils.lifecycleOwner
import com.base.wanandroid.viewmodel.request.RequestMineViewModel
import com.base.wanandroid.widget.Dialog
import com.blankj.utilcode.util.ToastUtils
import com.drake.serialize.intent.openActivity
import com.rainy.easybus.extention.observeEvent

/**
 * @author jiangshiyu
 * @date 2022/5/31
 * 我的界面
 */
class MineFragment : BaseFragment<UserViewModel, FragmentMineBinding>() {


    private lateinit var loginResultLaunch: ActivityResultLauncher<Intent>

    private val requestMeViewModel: RequestMineViewModel by viewModels()


    override fun initView(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)

        loginResultLaunch =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    if (AppConfig.UserName.isNotEmpty()) {
                        activity?.recreate()
                    }
                }
            }
        mViewBind.apply {
            if (AppConfig.UserName.isEmpty()) {
                headerImage.setOnClickListener {
                    loginResultLaunch.launch(LoginActivity.start(requireContext()))
                }
            }
            rankImage.setOnClickListener {
                //积分排行页
                openActivity<LeaderBoardActivity>()
            }
            mineIntegral.setOnClickListener {
                if (AppConfig.UserName.isEmpty()) {
                    ToastUtils.showShort(getString(R.string.please_login))
                    loginResultLaunch.launch(LoginActivity.start(requireContext()))
                } else {
                    //我的积分页
                    openActivity<IntegralActivity>()
                }
            }
            mineCollect.setOnClickListener {
                if (AppConfig.UserName.isEmpty()) {
                    ToastUtils.showShort(getString(R.string.please_login))
                    loginResultLaunch.launch(LoginActivity.start(requireContext()))
                } else {
                    //收藏界面
                    openActivity<CollectActivity>()
                }
            }
            mineShare.setOnClickListener {
                if (AppConfig.UserName.isEmpty()) {
                    ToastUtils.showShort(getString(R.string.please_login))
                    loginResultLaunch.launch(LoginActivity.start(requireContext()))
                } else {
                    //分享
                    openActivity<ShareActivity>()
                }
            }
            mineRecord.setOnClickListener { openActivity<HistoryRecordActivity>() }
            mineSetting.setOnClickListener { openActivity<SettingActivity>() }
            //未登录隐藏登出项，登陆可见
            mineExit.isVisible = CacheUtil.isLogin() == false
            mineExit.setOnClickListener {
                //登出弹窗确认
                Dialog.getConfirmDialog(
                    requireContext(),
                    getString(R.string.exit_confirm)
                ) { _, _ ->
                    mViewModel.loginOut()
                        .compose(RxTransformer.async())
                        .subscribe {
                            //从存储中清除cookie、个人信息
                            AppConfig.Cookie.clear()
                            AppConfig.UserName = ""
                            AppConfig.PassWord = ""
                            AppConfig.Level = ""
                            AppConfig.Rank = ""
                            AppConfig.CoinCount = ""
                            //重建页面
                            activity?.recreate()
                            ToastUtils.showShort(getString(R.string.exited_success))
                        }.lifecycleOwner(requireActivity())
                }.show()
            }
        }
    }

    override fun lazyLoadData() {

    }

    override fun createObserver() {
        viewLifecycleOwner.observeEvent<UserInfo> {
            mViewBind.apply {
                //用户名
                userText.text = it.userInfoResponse?.username
                //等级文字
                levelText.text = it.coinInfoResponse?.level.toString()
                //排名文字
                rankText.text = it.coinInfoResponse?.rank.toString()
                //积分项设置文本
                mineIntegral.setRightText(AppConfig.CoinCount.ifEmpty { "" })
            }
        }
    }

}