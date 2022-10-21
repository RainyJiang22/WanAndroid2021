package com.base.wanandroid.ui.adapter

import android.util.TypedValue
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.base.wanandroid.R
import com.base.wanandroid.data.TodoResponse
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @author jiangshiyu
 * @date 2022/10/21
 */
class TodoAdapter(data: ArrayList<TodoResponse>) :
    BaseQuickAdapter<TodoResponse, BaseViewHolder>(R.layout.item_todo, data) {
    init {
        setAnimationWithDefault(AnimationType.ScaleIn)
    }

    override fun convert(holder: BaseViewHolder, item: TodoResponse) {
        //赋值
        item.run {
            holder.setText(R.id.item_todo_title, title)
            holder.setText(R.id.item_todo_content, content)
            holder.setText(R.id.item_todo_date, dateStr)
            if (status == 1) {
                //已完成
                holder.setVisible(R.id.item_todo_status, true)
                holder.setImageResource(R.id.item_todo_status, R.drawable.ic_done)
                holder.getView<CardView>(R.id.item_todo_cardview).foreground = context.getDrawable(R.drawable.forground_shap)
            } else {
                if (isDone()) {
                    //未完成并且超过了预定完成时间
                    holder.setVisible(R.id.item_todo_status, true)
                    holder.setImageResource(R.id.item_todo_status, R.drawable.ic_yiguoqi)
                    holder.getView<CardView>(R.id.item_todo_cardview).foreground = context.getDrawable(R.drawable.forground_shap)
                } else {
                    //未完成
                    holder.setVisible(R.id.item_todo_status, false)
                    TypedValue().apply {
                        context.theme.resolveAttribute(R.attr.selectableItemBackground, this, true)
                    }.run {
                        holder.getView<CardView>(R.id.item_todo_cardview).foreground = context.getDrawable(resourceId)
                    }
                }
            }
        }
    }
}