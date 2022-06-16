package com.base.wanandroid.ui.adapter

import com.base.wanandroid.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @author jiangshiyu
 * @date 2022/6/16
 */
class SearchHistoryAdapter :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_search_history_list) {
    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.item_history_text, item)
    }
}