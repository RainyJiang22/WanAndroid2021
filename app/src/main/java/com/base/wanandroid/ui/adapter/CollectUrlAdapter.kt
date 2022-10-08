package com.base.wanandroid.ui.adapter

import com.base.wanandroid.R
import com.base.wanandroid.data.CollectUrlResponse
import com.base.wanandroid.widget.view.CollectView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import me.hgj.jetpackmvvm.ext.util.toHtml

/**
 * @author jiangshiyu
 * @date 2022/10/8
 */
class CollectUrlAdapter(data: ArrayList<CollectUrlResponse>) :
    BaseQuickAdapter<CollectUrlResponse, BaseViewHolder>(
        R.layout.item_collecturl, data
    ) {

    private var collectAction: (item: CollectUrlResponse, v: CollectView, position: Int) -> Unit =
        { _: CollectUrlResponse, _: CollectView, _: Int -> }

    init {
        setAnimationWithDefault(AnimationType.AlphaIn)
    }

    override fun convert(holder: BaseViewHolder, item: CollectUrlResponse) {
        //赋值
        item.run {
            holder.setText(R.id.item_collecturl_name, name.toHtml())
            holder.setText(R.id.item_collecturl_link, link)
            holder.getView<CollectView>(R.id.item_collecturl_collect).isChecked = true
        }
        holder.getView<CollectView>(R.id.item_collecturl_collect)
            .setOnClickListener(object : CollectView.OnClickListener {
                override fun onClick(v: CollectView) {
                    collectAction.invoke(item, v, holder.bindingAdapterPosition)
                }
            })
    }

    fun setCollectClick(inputCollectAction: (item: CollectUrlResponse, v: CollectView, position: Int) -> Unit) {
        this.collectAction = inputCollectAction
    }
}


