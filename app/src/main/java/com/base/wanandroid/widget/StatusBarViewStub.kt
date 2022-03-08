package com.base.wanandroid.widget

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import com.base.wanandroid.utils.ScreenUtil
import com.base.wanandroid.utils.dp
import com.gyf.immersionbar.ImmersionBar

class StatusBarViewStub : View {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var height = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) Math.max(
            24f.dp.toInt(),
            ScreenUtil.getStatusBarHeight(context)
        ) else 0
        if (context is Activity) {
            height = ImmersionBar.getStatusBarHeight(context as Activity)
        }
        super.onMeasure(
            widthMeasureSpec,
            MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        )
    }
}