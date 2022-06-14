package com.base.wanandroid.ui.mine

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import com.base.wanandroid.R
import com.base.wanandroid.base.BaseFragment
import com.base.wanandroid.databinding.FragmentMineBinding
import com.base.wanandroid.base.EmptyViewModel
import com.base.wanandroid.ui.history.HistoryRecordActivity
import com.base.wanandroid.ui.setting.SettingActivity
import com.base.wanandroid.ui.user.LoginActivity
import com.base.wanandroid.ui.user.UserViewModel
import com.base.wanandroid.utils.AppConfig
import com.base.wanandroid.utils.RxTransformer
import com.base.wanandroid.utils.lifecycleOwner
import com.base.wanandroid.widget.Dialog
import com.blankj.utilcode.util.ToastUtils
import com.drake.serialize.intent.openActivity

/**
 * @author jiangshiyu
 * @date 2022/5/31
 * 我的界面
 */
class MineFragment : BaseFragment<FragmentMineBinding, UserViewModel>() {


    private lateinit var loginResultLaunch: ActivityResultLauncher<Intent>


    override fun onBundle(bundle: Bundle) {
    }

    override fun init(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)

        loginResultLaunch =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    if (AppConfig.UserName.isNotEmpty()) {
                        activity?.recreate()
                    }
                }
            }
        binding?.apply {
            if (AppConfig.UserName.isEmpty()) {
                headerImage.setOnClickListener {
                    loginResultLaunch.launch(LoginActivity.start(requireContext()))
                }
            }
            rankImage.setOnClickListener {
                //TODO 积分排行页
            }
            mineIntegral.setOnClickListener {
                if (AppConfig.UserName.isEmpty()) {
                    ToastUtils.showShort(getString(R.string.please_login))
                    loginResultLaunch.launch(LoginActivity.start(requireContext()))
                } else {
                    //TODO 我的积分页
                }
            }
            mineCollect.setOnClickListener {
                if (AppConfig.UserName.isEmpty()) {
                    ToastUtils.showShort(getString(R.string.please_login))
                    loginResultLaunch.launch(LoginActivity.start(requireContext()))
                } else {
                    //TODO 收藏界面
                }
            }
            mineShare.setOnClickListener {
                if (AppConfig.UserName.isEmpty()) {
                    ToastUtils.showShort(getString(R.string.please_login))
                    loginResultLaunch.launch(LoginActivity.start(requireContext()))
                } else {
                    //TODO 分享
                }
            }
            mineRecord.setOnClickListener { openActivity<HistoryRecordActivity>() }
            mineSetting.setOnClickListener { openActivity<SettingActivity>() }
            //未登录隐藏登出项，登陆可见
            mineExit.isVisible = AppConfig.CoinCount.isNotEmpty()
            mineExit.setOnClickListener {
                //登出弹窗确认
                Dialog.getConfirmDialog(
                    requireContext(),
                    getString(R.string.exit_confirm)
                ) { _, _ ->
                    viewModel.loginOut()
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


    override fun onResume() {
        super.onResume()
        binding?.apply {
            toolbar.title = getString(R.string.mine_fragment)
            //用户名
            userText.text = AppConfig.UserName.ifEmpty { getString(R.string.my_user) }
            //等级文字
            levelText.text = AppConfig.Level.ifEmpty { getString(R.string.my_ellipsis) }
            //排名文字
            rankText.text = AppConfig.Rank.ifEmpty { getString(R.string.my_ellipsis) }
            //积分项设置文本
            mineIntegral.setRightText(AppConfig.CoinCount.ifEmpty { "" })
        }
    }
}