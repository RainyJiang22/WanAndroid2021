package com.base.wanandroid.ui.history

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.base.wanandroid.R
import com.base.wanandroid.base.BaseActivity
import com.base.wanandroid.databinding.ActivityHistoryRecordBinding
import com.base.wanandroid.history.HistoryRepository
import com.base.wanandroid.ui.adapter.HistoryRecordAdapter
import com.base.wanandroid.ui.web.WebActivity
import com.base.wanandroid.utils.RxTransformer
import com.base.wanandroid.utils.lifecycleOwner
import com.blankj.utilcode.util.ToastUtils
import com.example.wanAndroid.widget.decoration.SpaceItemDecoration

/**
 * @author jiangshiyu
 * @date 2022/6/9
 * 历史记录页面
 */
class HistoryRecordActivity : BaseActivity<ActivityHistoryRecordBinding, HistoryRecordViewModel>() {


    private val historyRecordAdapter by lazy {
        HistoryRecordAdapter().apply {
            this.setDiffCallback(HistoryRecordDiffCallBack())
        }
    }

    override fun onBundle(bundle: Bundle) {
    }

    override fun init(savedInstanceState: Bundle?) {


        binding?.titleBar?.let {

            it.leftView.setOnClickListener {
                finishAfterTransition()
            }

            it.rightView.setOnClickListener {
                //清空所有历史记录
                HistoryRepository.deleteAllHistoryRecord()
                    .compose(RxTransformer.asyncCompletable())
                    .subscribe {
                        historyRecordAdapter.setDiffNewData(null)
                        ToastUtils.showShort(R.string.delete_succeed)
                    }.lifecycleOwner(this)
            }
        }

        initData()

    }

    private fun initData() {
        binding?.rv?.let {
            it.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            it.addItemDecoration(SpaceItemDecoration(this))
            it.adapter = historyRecordAdapter
        }
        historyRecordAdapter.setOnItemClickListener { _, _, position ->
            val item = historyRecordAdapter.data[position]
            WebActivity.start(this, item.url)
        }

        viewModel.historyRecordList
            .compose(RxTransformer.async())
            .subscribe {
                historyRecordAdapter.setDiffNewData(it.toMutableList())
            }.lifecycleOwner(this)
    }


}