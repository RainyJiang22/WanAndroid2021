package com.base.wanandroid.base

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.base.wanandroid.utils.binding
import com.photoroom.editor.base.WindowParam
import com.photoroom.editor.base.WindowParams
import java.lang.reflect.ParameterizedType


abstract class SimpleDialogFragment<V : ViewBinding> : DialogFragment() {

    var binding: V? = null

    private lateinit var mDialog: Dialog

    private var hasInflate = false


    private var onWindow: (Window.() -> Unit)? = null

    private var onView: (View.() -> Unit)? = null

    abstract fun build(savedInstanceState: Bundle?)

    private var owner: LifecycleOwner? = null

    private fun init(owner: LifecycleOwner) {
        this.owner = owner
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
        dismissAllowingStateLoss()
    }

    fun onWindow(onWindow: (Window.() -> Unit)): SimpleDialogFragment<V> {
        this.onWindow = onWindow
        return this
    }

    inline fun <reified T : ViewBinding> View.onBindingView(onBindingView: (T.() -> Unit)) {
        onBindingView.invoke(binding<T>(this)!!)
    }


    fun onView(onView: (View.() -> Unit)): SimpleDialogFragment<V> {
        this.onView = onView
        return this
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is LifecycleOwner) {
            init(context)
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val param = javaClass.getAnnotation(WindowParam::class.java)?.let {
            WindowParams(
                it.gravity,
                it.styleName,
                it.outSideCanceled,
                it.animRes,
                it.canceled,
                it.dimAmount
            )
        } ?: WindowParams()
        if (hasInflate) {
            return mDialog
        } else {
            mDialog = if (param.styleName.isNotEmpty()) {
                val styleId =
                    resources.getIdentifier(param.styleName, "style", context?.packageName)
                Dialog(requireContext(), styleId)
            } else {
                Dialog(requireContext())
            }
        }
        build(savedInstanceState)
        val container = FrameLayout(requireContext())
        val rootView = getContentView(container, true)
        if (rootView != null) {
            val gravity = param.gravity
            val outSideCanceled = param.outSideCanceled
            val canceled = param.canceled
            val dimAmount = param.dimAmount
            val animRes = param.animRes
            val dm = resources.displayMetrics
            rootView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            val childLayoutParams = rootView.layoutParams as ViewGroup.MarginLayoutParams

            val layoutParamsWidth = childLayoutParams.width
            val layoutParamsHeight = childLayoutParams.height


            val width: Int = when (layoutParamsWidth) {
                ViewGroup.LayoutParams.MATCH_PARENT -> {
                    dm.widthPixels - childLayoutParams.leftMargin - childLayoutParams.rightMargin
                }
                ViewGroup.LayoutParams.WRAP_CONTENT -> {
                    rootView.measuredWidth
                }
                else -> {
                    layoutParamsWidth
                }
            }

            val height: Int = when (layoutParamsHeight) {
                ViewGroup.LayoutParams.MATCH_PARENT -> {
                    dm.heightPixels - childLayoutParams.topMargin - childLayoutParams.bottomMargin
                }
                ViewGroup.LayoutParams.WRAP_CONTENT -> {
                    rootView.measuredHeight
                }
                else -> {
                    layoutParamsHeight
                }
            }

            mDialog.setContentView(container)
            mDialog.setCanceledOnTouchOutside(outSideCanceled)
            mDialog.setCancelable(canceled)
            isCancelable = canceled

            val window = mDialog.window
            window?.apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.statusBarColor = Color.TRANSPARENT
                }


                setLayout(width, height)

                setBackgroundDrawable(ColorDrawable(0x00000000))
                setGravity(gravity)
                if (animRes != -1) {
                    setWindowAnimations(animRes)
                }
                if (dimAmount != -1f) {
                    setDimAmount(dimAmount)
                }
                onWindow?.invoke(this)
            }

            onView?.invoke(container)

            hasInflate = true
        }
        return mDialog
    }


    private fun getContentView(group: ViewGroup, attach: Boolean): View? {
        val types = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments
        val aClass = types[0] as Class<V>
        try {
            binding =
                aClass.getDeclaredMethod(
                    "inflate",
                    LayoutInflater::class.java,
                    ViewGroup::class.java,
                    Boolean::class.java
                )
                    .invoke(null, layoutInflater, group, attach) as V?
            return binding?.root
        } catch (e: Error) {
            e.printStackTrace();
        }
        return null
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            if (!isAdded) {
                val transaction = manager.beginTransaction()
                transaction.add(this, tag)
                transaction.commitNowAllowingStateLoss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}