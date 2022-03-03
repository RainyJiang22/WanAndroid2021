package com.base.wanandroid.ui


import android.os.Bundle
import com.base.wanandroid.base.BaseActivity
import com.base.wanandroid.databinding.ActivityMainBinding
import com.photoroom.editor.base.EmptyViewModel

class MainActivity : BaseActivity<ActivityMainBinding, EmptyViewModel>() {
    override fun onBundle(bundle: Bundle) {

    }

    override fun init(savedInstanceState: Bundle?) {

        println("hello world")
    }

}