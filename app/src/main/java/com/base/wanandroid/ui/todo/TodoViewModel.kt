package com.base.wanandroid.ui.todo

import com.base.wanandroid.viewmodel.request.RequestTodoViewModel
import me.hgj.jetpackmvvm.callback.databind.IntObservableField
import me.hgj.jetpackmvvm.callback.databind.StringObservableField

/**
 * @author jiangshiyu
 * @date 2022/10/21
 */
class TodoViewModel : RequestTodoViewModel() {

    //标题
    var todoTitle = StringObservableField()

    //内容
    var todoContent =
        StringObservableField()

    //时间
    var todoTime = StringObservableField()

    //优先级
    var todoLeve =
        StringObservableField(TodoType.TodoType1.content)

    //优先级颜色
    var todoColor =
        IntObservableField(TodoType.TodoType1.color)

}