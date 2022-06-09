package com.base.wanandroid.base

import android.view.Gravity

@Target(AnnotationTarget.CLASS)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class  WindowParam(val gravity:Int = Gravity.CENTER,
                              val styleName :String = "BaseDialogStyle",
                              val outSideCanceled:Boolean = true,val animRes :Int = -1,
                              val canceled:Boolean = true, val dimAmount :Float = -1f)

data class WindowParams(val gravity:Int = Gravity.CENTER,
                        val styleName :String = "BaseDialogStyle",
                        val outSideCanceled:Boolean = true,val animRes :Int = -1,
                        val canceled:Boolean = true, val dimAmount :Float = 0.3f)

