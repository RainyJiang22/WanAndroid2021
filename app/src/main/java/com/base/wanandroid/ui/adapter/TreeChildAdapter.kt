package com.base.wanandroid.ui.adapter

import android.os.Build
import androidx.annotation.RequiresApi
import com.base.wanandroid.R
import com.base.wanandroid.data.ClassifyResponse
import com.base.wanandroid.utils.html2Spanned
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @author jiangshiyu
 * @date 2022/6/6
 */
class TreeChildAdapter(dataList: MutableList<ClassifyResponse>) :
    BaseQuickAdapter<ClassifyResponse, BaseViewHolder>(R.layout.item_flow_text, dataList) {
    init {
        //设置默认加载动画
        setAnimationWithDefault(AnimationType.ScaleIn)
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun convert(holder: BaseViewHolder, item: ClassifyResponse) {
        //标签文字
        holder.setText(R.id.flow_tag, item.name.html2Spanned())
    }
}