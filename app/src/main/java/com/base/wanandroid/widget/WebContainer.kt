package com.base.wanandroid.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.base.wanandroid.R
import com.base.wanandroid.utils.alphaColor

/**
 * @author jiangshiyu
 * @date 2022/6/2
 * web容器视图
 */
class WebContainer : CoordinatorLayout {

    private val mMaskColor by lazy {
        alphaColor(
            ContextCompat.getColor(
                context,
                R.color.color_web_bg_draw
            ), 1f
        )
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        canvas.drawColor(mMaskColor)
    }
}