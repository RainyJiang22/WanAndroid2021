package com.base.wanandroid.ui.adapter

import com.base.wanandroid.R
import com.base.wanandroid.data.ArticleResponse
import com.base.wanandroid.ui.web.WebActivity
import com.base.wanandroid.utils.randomColor
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @author jiangshiyu
 * @date 2022/6/7
 */
class NavigationChildAdapter(data: MutableList<ArticleResponse>) :
    BaseQuickAdapter<ArticleResponse, BaseViewHolder>(R.layout.item_flow_shape, data) {

    init {
        //设置默认加载动画
        setAnimationWithDefault(AnimationType.ScaleIn)
        //设置Item点击事件
        this.setOnItemClickListener { _, _, position ->
            data[position].run { WebActivity.start(context, id, title, link, collect) }
        }
    }

    override fun convert(holder: BaseViewHolder, item: ArticleResponse) {
        holder.setText(R.id.flow_tag,item.title)
        holder.setTextColor(R.id.flow_tag, randomColor())
    }
}