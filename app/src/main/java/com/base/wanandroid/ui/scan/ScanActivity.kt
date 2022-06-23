package com.base.wanandroid.ui.scan

import android.os.Bundle
import androidx.core.content.ContextCompat
import cn.bingoogolapple.qrcode.core.QRCodeView
import com.base.wanandroid.R
import com.base.wanandroid.base.BaseActivity
import com.base.wanandroid.base.EmptyViewModel
import com.base.wanandroid.databinding.ActivityScanBinding
import com.base.wanandroid.ui.web.WebActivity
import com.blankj.utilcode.util.ToastUtils
import com.huantansheng.easyphotos.EasyPhotos
import com.huantansheng.easyphotos.callback.SelectCallback
import com.huantansheng.easyphotos.models.album.entity.Photo

/**
 * @author jiangshiyu
 * @date 2022/6/23
 */
class ScanActivity : BaseActivity<ActivityScanBinding, EmptyViewModel>(), QRCodeView.Delegate {

    private var isChecked = false

    override fun onBundle(bundle: Bundle) {
    }

    override fun init(savedInstanceState: Bundle?) {


        binding?.flash?.apply {
            setOnClickListener {
                isChecked = !isChecked
                if (isChecked) {
                    setImageDrawable(
                        ContextCompat.getDrawable(
                            this@ScanActivity,
                            R.drawable.ic_flash_on
                        )
                    )
                    binding?.zxingView?.openFlashlight()
                } else {
                    setImageDrawable(
                        ContextCompat.getDrawable(
                            this@ScanActivity,
                            R.drawable.ic_flash_off
                        )
                    )
                    binding?.zxingView?.closeFlashlight()
                }
            }
        }

        //相册按钮点击监听
        binding?.fab?.setOnClickListener {
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
                                binding?.zxingView?.decodeQRCode(it.path)
                            }
                        }

                        override fun onCancel() {}
                    })
            }
        }
        binding?.zxingView?.setDelegate(this)
    }

    override fun onStart() {
        super.onStart()
        binding?.zxingView?.startCamera() // 打开后置摄像头开始预览，但是并未开始识别
        binding?.zxingView?.startSpotAndShowRect() // 显示扫描框，并开始识别
    }

    override fun onStop() {
        binding?.zxingView?.stopCamera() // 关闭摄像头预览，并且隐藏扫描框
        super.onStop()
    }

    override fun onDestroy() {
        binding?.zxingView?.onDestroy() // 销毁二维码扫描控件
        super.onDestroy()
    }

    override fun onScanQRCodeSuccess(result: String?) {
        //解析失败
        if (result.isNullOrEmpty()) {
            ToastUtils.showShort(R.string.identify_error)
            binding?.zxingView?.startSpot()//继续识别
            return
        }
        ToastUtils.showShort(R.string.identify_succeed)
        WebActivity.start(this, result)
        finish()
    }

    override fun onCameraAmbientBrightnessChanged(isDark: Boolean) {
        //环境是否过暗状态
        var tipText = binding?.zxingView?.scanBoxView?.tipText
        val ambientBrightnessTip = getString(R.string.open_flash)
        if (isDark) {
            if (tipText?.contains(ambientBrightnessTip) == false) {
                binding?.zxingView?.scanBoxView?.tipText = tipText + ambientBrightnessTip
            }
        } else {
            if (tipText?.contains(ambientBrightnessTip) == true) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip))
                binding?.zxingView?.scanBoxView?.tipText = tipText
            }
        }
    }

    override fun onScanQRCodeOpenCameraError() {
        ToastUtils.showShort(R.string.open_camera_failed)
    }
}