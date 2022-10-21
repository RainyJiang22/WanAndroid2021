package com.base.wanandroid.ui.adapter

import com.base.wanandroid.R
import com.base.wanandroid.ui.todo.TodoType
import com.base.wanandroid.widget.MyColorCircleView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class PriorityAdapter(data: ArrayList<TodoType>) : BaseQuickAdapter<TodoType, BaseViewHolder>(R.layout.item_todo_dialog, data) {
    var checkType = TodoType.TodoType1.type

    constructor(data: ArrayList<TodoType>, checkType: Int) : this(data) {
        this.checkType = checkType
    }

    override fun convert(holder: BaseViewHolder, item: TodoType) {
        //赋值
        item.run {
            holder.setText(R.id.item_todo_dialog_name, item.content)
            if (checkType == item.type) {
                holder.getView<MyColorCircleView>(R.id.item_todo_dialog_icon).setViewSelect(item.color)
            } else {
                holder.getView<MyColorCircleView>(R.id.item_todo_dialog_icon).setView(item.color)
            }
        }
    }
}