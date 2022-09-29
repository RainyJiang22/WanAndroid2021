package com.base.wanandroid.ui.scan

import android.os.Bundle
import androidx.core.content.ContextCompat
import cn.bingoogolapple.qrcode.core.QRCodeView
import com.base.wanandroid.R
import com.base.wanandroid.base.BaseActivity
import com.base.wanandroid.databinding.ActivityScanBinding
import com.base.wanandroid.ui.web.WebActivity
import com.blankj.utilcode.util.ToastUtils
import com.huantansheng.easyphotos.EasyPhotos
import com.huantansheng.easyphotos.callback.SelectCallback
import com.huantansheng.easyphotos.models.album.entity.Photo
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * @author jiangshiyu
 * @date 2022/6/23
 * 二维码扫描页
 */
class ScanActivity : BaseActivity<BaseViewModel, ActivityScanBinding>(), QRCodeView.Delegate {

    private var isChecked = false


    override fun initView(savedInstanceState: Bundle?) {


        mViewBind.flash.apply {
            setOnClickListener {
                isChecked = !isChecked
                if (isChecked) {
                    setImageDrawable(
                        ContextCompat.getDrawable(
                            this@ScanActivity,
                            R.drawable.ic_flash_on
                        )
                    )
                    mViewBind.zxingView.openFlashlight()
                } else {
                    setImageDrawable(
                        ContextCompat.getDrawable(
                            this@ScanActivity,
                            R.drawable.ic_flash_off
                        )
                    )
                    mViewBind.zxingView.closeFlashlight()
                }
            }
        }

        //相册按钮点击监听
        mViewBind.fab.setOnClickListener {
            //使用Glide图片加载引擎
            GlideEngine.instance?.let { its ->
                //参数说明：上下文，是否显示相机按钮，是否使用宽高数据（false时宽高数据为0，扫描速度更快），[配置Glide为图片加载引擎]
                EasyPhotos.createAlbum(this, true, false, its)
                    //参数说明：见下方`FileProvider的配置`
                    .setFileProviderAuthority("com.example.wanAndroid.ui.provider.FileProvider")
                    //无拼图功能
                    .setPuzzleMenu(false)
                    //参数说明：最大可选数，默认1
                    .setCount(1)
                    //相册回调
                    .start(object : SelectCallback() {
                        //photos:返回对象集合：如果你需要了解图片的宽、高、大小、用户是否选中原图选项等信息，可以用这个
                        override fun onResult(photos: ArrayList<Photo>, isOriginal: Boolean) {
                            photos.forEach {
                                //根据图片路径解析二维码
                                mViewBind.zxingView.decodeQRCode(it.path)
                            }
                        }

                        override fun onCancel() {}
                    })
            }
        }
        mViewBind.zxingView.setDelegate(this)
    }

    override fun onStart() {
        super.onStart()
        mViewBind.zxingView.startCamera() // 打开后置摄像头开始预览，但是并未开始识别
        mViewBind.zxingView.startSpotAndShowRect() // 显示扫描框，并开始识别
    }

    override fun onStop() {
        mViewBind.zxingView.stopCamera() // 关闭摄像头预览，并且隐藏扫描框
        super.onStop()
    }

    override fun onDestroy() {
        mViewBind.zxingView.onDestroy() // 销毁二维码扫描控件
        super.onDestroy()
    }

    override fun onScanQRCodeSuccess(result: String?) {
        //解析失败
        if (result.isNullOrEmpty()) {
            ToastUtils.showShort(R.string.identify_error)
            mViewBind.zxingView.startSpot()//继续识别
            return
        }
        ToastUtils.showShort(R.string.identify_succeed)
        WebActivity.start(this, result)
        finish()
    }

    override fun onCameraAmbientBrightnessChanged(isDark: Boolean) {
        //环境是否过暗状态
        var tipText = mViewBind.zxingView.scanBoxView?.tipText
        val ambientBrightnessTip = getString(R.string.open_flash)
        if (isDark) {
            if (tipText?.contains(ambientBrightnessTip) == false) {
                mViewBind.zxingView.scanBoxView?.tipText = tipText + ambientBrightnessTip
            }
        } else {
            if (tipText?.contains(ambientBrightnessTip) == true) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip))
                mViewBind.zxingView.scanBoxView?.tipText = tipText
            }
        }
    }

    override fun onScanQRCodeOpenCameraError() {
        ToastUtils.showShort(R.string.open_camera_failed)
    }
}