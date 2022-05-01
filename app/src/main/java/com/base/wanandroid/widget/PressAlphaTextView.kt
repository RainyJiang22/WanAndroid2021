package com.base.wanandroid.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * @author jiangshiyu
 * @date 2022/4/1
 *
 * 长按半透明松手恢复的TextView
 */
class PressAlphaTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {


    override fun dispatchSetPressed(pressed: Boolean) {

        //判断手指是否按下
        alpha = if (pressed) 0.5f else 1f
    }
}