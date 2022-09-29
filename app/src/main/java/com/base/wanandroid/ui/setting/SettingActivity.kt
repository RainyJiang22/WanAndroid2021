package com.base.wanandroid.ui.setting

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import com.base.wanandroid.R
import com.base.wanandroid.base.BaseActivity1
import com.base.wanandroid.databinding.ActivitySettingBinding
import com.base.wanandroid.ui.scan.ScanActivity
import com.base.wanandroid.ui.web.WebActivity
import com.base.wanandroid.utils.AppConfig
import com.base.wanandroid.utils.CacheDataUtil
import com.base.wanandroid.widget.Dialog
import com.blankj.utilcode.util.ToastUtils
import com.drake.serialize.intent.openActivity
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * @author jiangshiyu
 * @date 2022/6/9
 */
class SettingActivity : BaseActivity1<BaseViewModel, ActivitySettingBinding>() {


    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.titleBar.leftView?.setOnClickListener {
            finishAfterTransition()
        }

        mViewBind.settingDark.isChecked = AppConfig.DarkTheme
        mViewBind.settingDark.setOnCheckedChangeListener { _, isChecked ->
            AppConfig.DarkTheme = if (isChecked) {
                //全局设置夜间模式
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                false
            }

        }

        mViewBind.settingClear.setRightText(CacheDataUtil.getTotalCacheSize(this))
        //清除缓存
        mViewBind.settingClear.setOnClickListener {
            Dialog.getConfirmDialog(this, getString(R.string.clear_cache)) { _, _ ->
                CacheDataUtil.clearAllCache(this)
                ToastUtils.showShort(getString(R.string.clear_success))
                mViewBind.settingClear.setRightText(CacheDataUtil.getTotalCacheSize(this))
            }.show()
        }

        mViewBind.settingScan.setOnClickListener {
            //扫码
            XXPermissions.with(this).permission(Permission.CAMERA).request { _, all ->
                if (all) {
                    openActivity<ScanActivity>()
                }
            }
        }

        //版本号
        val packageInfo = packageManager.getPackageInfo(this.packageName, 0)
        mViewBind.settingUpdate.setRightText(
            getString(
                R.string.setting_version,
                packageInfo.versionName
            )
        )
        //版本更新
        mViewBind.settingUpdate.setOnClickListener {}
        //官方网站
        mViewBind.settingWeb.setOnClickListener {
            WebActivity.start(
                this,
                getString(R.string.setting_site)
            )
        }
        //项目源码
        mViewBind.settingProject.setOnClickListener {
            WebActivity.start(
                this,
                getString(R.string.setting_repository)
            )
        }
        //版权声明
        mViewBind.settingCopyright.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle(R.string.setting_copyright)
                .setMessage(R.string.copyright_content)
                .setCancelable(true)
                .show()
        }
        //关于我们
        mViewBind.settingAbout.setOnClickListener {
            val about = AboutMeFragment()
            about.show(supportFragmentManager, about.tag)
        }
    }
}