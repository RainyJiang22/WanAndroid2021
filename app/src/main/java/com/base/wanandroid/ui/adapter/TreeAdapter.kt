package com.base.wanandroid.ui.adapter

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.base.wanandroid.R
import com.base.wanandroid.utils.html2Spanned
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.wanAndroid.logic.model.SystemResponse
import com.google.android.flexbox.FlexboxLayoutManager

/**
 * @author jiangshiyu
 * @date 2022/6/6
 */
class TreeAdapter : BaseQuickAdapter<SystemResponse, BaseViewHolder>(R.layout.item_system_list) {

    init {
        //默认加载动画
        setAnimationWithDefault(AnimationType.ScaleIn)
    }

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        super.onItemViewHolderCreated(viewHolder, viewType)
        viewHolder.getView<RecyclerView>(R.id.rv).run {
            //使用伸缩布局
            layoutManager = FlexboxLayoutManager(context)
            //避免item改变重新绘制rv
            setHasFixedSize(true)
            //禁用嵌套滚动
            isNestedScrollingEnabled = false
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun convert(holder: BaseViewHolder, item: SystemResponse) {
        //设置rv标题
        holder.setText(R.id.title, item.name.html2Spanned())
        //使用子流标签适配器
        holder.getView<RecyclerView>(R.id.rv).adapter = TreeChildAdapter(item.children).run {
            //给子项适配器设置点击事件
            setOnItemClickListener { _, _, position ->
                //todo 打开体系页面 2022/6/6
            }
            this
        }


    }


}