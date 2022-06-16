package com.base.wanandroid.ui.adapter

import com.base.wanandroid.R
import com.base.wanandroid.bean.SearchHotResponse
import com.base.wanandroid.utils.randomColor
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @author jiangshiyu
 * @date 2022/6/16
 */
class SearchHotAdapter :
    BaseQuickAdapter<SearchHotResponse, BaseViewHolder>(R.layout.item_flow_shape) {
    override fun convert(holder: BaseViewHolder, item: SearchHotResponse) {
        holder.setText(R.id.flow_tag, item.name)
        holder.setTextColor(R.id.flow_tag, randomColor())
    }
}