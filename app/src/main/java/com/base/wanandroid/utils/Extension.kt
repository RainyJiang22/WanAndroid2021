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


fun FragmentActivity.replaceFragment(replaceFragment: Fragment, tag: String, id: Int = R.id.frame) {
    var tempFragment = supportFragmentManager.findFragmentByTag(tag)
    val transaction = supportFragmentManager.beginTransaction()
    if (tempFragment == null) {
        try {
            tempFragment = replaceFragment.apply {
                enterTransition = createTransition()
            }
            transaction
                .add(id, tempFragment, tag)
                .setMaxLifecycle(tempFragment, Lifecycle.State.RESUMED)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    val fragments = supportFragmentManager.fragments

    for (i in fragments.indices) {
        val fragment = fragments[i]
        if (fragment.tag == tag) {
            transaction
                .show(fragment)
        } else {
            transaction
                .hide(fragment)
        }
    }
    transaction.commitAllowingStateLoss()
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
