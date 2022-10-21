package com.base.wanandroid.utils

import android.content.Context
import android.content.res.Resources
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.Html
import android.text.Spanned
import android.util.TypedValue
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.base.wanandroid.R
import com.base.wanandroid.application.WanAndroidApplication

val Float.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    )

val Float.px
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_PX,
        this,
        Resources.getSystem().displayMetrics
    )

val Float.sp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this,
        Resources.getSystem().displayMetrics
    )

val Int.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )

fun Uri.getRatio(context: Context): Float {
    try {
        val cursor: Cursor? = context.contentResolver.query(
            this,
            arrayOf(
                MediaStore.Images.ImageColumns.WIDTH,
                MediaStore.Images.ImageColumns.HEIGHT
            ),
            null, null, null
        )
        var result = 1.0f
        if (null != cursor) {
            if (cursor.moveToFirst()) {
                val width = cursor.getString(0)
                val height = cursor.getString(1)
                val ratio = width.toFloat() / height.toFloat()
                result = ratio
            }
            cursor.close()
        }
        return result
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        return 1.0f
    }
}

val screenWidth = Resources.getSystem().displayMetrics.widthPixels

val screenHeight = Resources.getSystem().displayMetrics.heightPixels

@ColorInt
fun Context.color(colorId: Int): Int {
    return ContextCompat.getColor(this, colorId)
}


private fun createTransition(): androidx.transition.TransitionSet {
    val transitionSet = androidx.transition.TransitionSet()
    transitionSet.interpolator = LinearInterpolator()
    transitionSet.duration = 300
    transitionSet.addTransition(androidx.transition.Fade())
    return transitionSet
}

//Toast
fun Int.toast(duration: Int) {
    Toast.makeText(WanAndroidApplication.getApplication(), this, duration).show()
}

fun Int.toastShort() {
    toast(Toast.LENGTH_SHORT)
}

fun Int.toastLong() {
    toast(Toast.LENGTH_LONG)
}

/**
 * 判断是否为空 并传入相关操作
 */
inline fun <reified T> T?.notNull(notNullAction: (T) -> Unit, nullAction: () -> Unit = {}) {
    if (this != null) {
        notNullAction.invoke(this)
    } else {
        nullAction.invoke()
    }
}

/**
 * @param message 显示对话框的内容 必填项
 * @param title 显示对话框的标题 默认 温馨提示
 * @param positiveButtonText 确定按钮文字 默认确定
 * @param positiveAction 点击确定按钮触发的方法 默认空方法
 * @param negativeButtonText 取消按钮文字 默认空 不为空时显示该按钮
 * @param negativeAction 点击取消按钮触发的方法 默认空方法
 */
fun Fragment.showMessage(
    message: String,
    title: String = "温馨提示",
    positiveButtonText: String = "确定",
    positiveAction: () -> Unit = {},
    negativeButtonText: String = "",
    negativeAction: () -> Unit = {}
) {
    activity?.let {
        MaterialDialog(it)
            .cancelable(true)
            .lifecycleOwner(viewLifecycleOwner)
            .show {
                title(text = title)
                message(text = message)
                positiveButton(text = positiveButtonText) {
                    positiveAction.invoke()
                }
                if (negativeButtonText.isNotEmpty()) {
                    negativeButton(text = negativeButtonText) {
                        negativeAction.invoke()
                    }
                }
                getActionButton(WhichButton.POSITIVE).updateTextColor(SettingUtil.getColor(it))
                getActionButton(WhichButton.NEGATIVE).updateTextColor(SettingUtil.getColor(it))
            }
    }
}