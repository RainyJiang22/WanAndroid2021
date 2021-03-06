package com.base.wanandroid.ui.setting

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import com.base.wanandroid.R
import com.base.wanandroid.base.BaseActivity
import com.base.wanandroid.databinding.ActivitySettingBinding
import com.base.wanandroid.base.EmptyViewModel
import com.base.wanandroid.ui.scan.ScanActivity
import com.base.wanandroid.ui.web.WebActivity
import com.base.wanandroid.utils.AppConfig
import com.base.wanandroid.utils.CacheDataUtil
import com.base.wanandroid.widget.Dialog
import com.blankj.utilcode.util.ToastUtils
import com.drake.serialize.intent.openActivity
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions

/**
 * @author jiangshiyu
 * @date 2022/6/9
 */
class SettingActivity : BaseActivity<ActivitySettingBinding, EmptyViewModel>() {
    override fun onBundle(bundle: Bundle) {

    }

    override fun init(savedInstanceState: Bundle?) {

        binding?.titleBar?.leftView?.setOnClickListener {
            finishAfterTransition()
        }

        binding?.settingDark?.isChecked = AppConfig.DarkTheme
        binding?.settingDark?.setOnCheckedChangeListener { _, isChecked ->
            AppConfig.DarkTheme = if (isChecked) {
                //全局设置夜间模式
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                false
            }

        }

        binding?.settingClear?.setRightText(CacheDataUtil.getTotalCacheSize(this))
        //清除缓存
        binding?.settingClear?.setOnClickListener {
            Dialog.getConfirmDialog(this, getString(R.string.clear_cache)) { _, _ ->
                CacheDataUtil.clearAllCache(this)
                ToastUtils.showShort(getString(R.string.clear_success))
                binding?.settingClear?.setRightText(CacheDataUtil.getTotalCacheSize(this))
            }.show()
        }

        binding?.settingScan?.setOnClickListener {
            //扫码
            XXPermissions.with(this).permission(Permission.CAMERA).request { _, all ->
                if (all) {
                    openActivity<ScanActivity>()
                }
            }
        }

        //版本号
        val packageInfo = packageManager.getPackageInfo(this.packageName, 0)
        binding?.settingUpdate?.setRightText(
            getString(
                R.string.setting_version,
                packageInfo.versionName
            )
        )
        //版本更新
        binding?.settingUpdate?.setOnClickListener {}
        //官方网站
        binding?.settingWeb?.setOnClickListener {
            WebActivity.start(
                this,
                getString(R.string.setting_site)
            )
        }
        //项目源码
        binding?.settingProject?.setOnClickListener {
            WebActivity.start(
                this,
                getString(R.string.setting_repository)
            )
        }
        //版权声明
        binding?.settingCopyright?.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle(R.string.setting_copyright)
                .setMessage(R.string.copyright_content)
                .setCancelable(true)
                .show()
        }
        //关于我们
        binding?.settingAbout?.setOnClickListener {
            val about = AboutMeFragment()
            about.show(supportFragmentManager, about.tag)
        }


    }
}