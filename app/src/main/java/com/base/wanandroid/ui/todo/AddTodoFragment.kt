package com.base.wanandroid.ui.todo

import android.os.Bundle
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.base.wanandroid.base.BaseDbFragment
import com.base.wanandroid.base.BaseFragment
import com.base.wanandroid.data.TodoResponse
import com.base.wanandroid.databinding.FragmentTodoAddBinding
import com.base.wanandroid.utils.DatetimeUtil
import com.base.wanandroid.utils.notNull
import com.base.wanandroid.utils.showMessage
import com.base.wanandroid.widget.PriorityDialog
import com.blankj.utilcode.util.ToastUtils
import me.hgj.jetpackmvvm.ext.nav
import java.util.*

/**
 * @author jiangshiyu
 * @date 2022/10/21
 */
class AddTodoFragment : BaseDbFragment<TodoViewModel, FragmentTodoAddBinding>() {

    private var todoResponse: TodoResponse? = null

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.vm = mViewModel
        mDatabind.click = ProxyClick()
        arguments?.let {
            todoResponse = it.getParcelable("todo")
            todoResponse?.let { todo ->
                mViewModel.todoTitle.set(todo.title)
                mViewModel.todoContent.set(todo.content)
                mViewModel.todoTime.set(todo.dateStr)
                mViewModel.todoLeve.set(TodoType.byType(todo.priority).content)
                mViewModel.todoColor.set(TodoType.byType(todo.priority).color)
            }
        }

        mDatabind.titleBar.leftView.setOnClickListener {
            nav().navigateUp()
        }

    }


    override fun createObserver() {

        mViewModel.updateDataState.observe(viewLifecycleOwner) {
            if (it.isSuccess) {
                //添加TODO成功,返回并发送消息回调
                nav().navigateUp()
            } else {
                ToastUtils.showShort(it.errorMsg)
            }
        }

    }


    inner class ProxyClick {

        //选择时间
        fun todoTime() {
            activity?.let {
                MaterialDialog(it)
                    .lifecycleOwner(this@AddTodoFragment).show {
                        cornerRadius(0f)
                        datePicker(minDate = Calendar.getInstance()) { dialog, date ->
                            mViewModel.todoTime.set(
                                DatetimeUtil.formatDate(
                                    date.time,
                                    DatetimeUtil.DATE_PATTERN
                                )
                            )
                        }
                    }
            }
        }

        //选择类型
        fun todoType() {
            activity?.let {
                PriorityDialog(it, TodoType.byValue(mViewModel.todoLeve.get()).type).apply {
                    setPriorityInterface(object : PriorityDialog.PriorityInterface {
                        override fun onSelect(type: TodoType) {
                            mViewModel.todoLeve.set(type.content)
                            mViewModel.todoColor.set(type.color)
                        }
                    })
                }.show()
            }
        }

        //提交
        fun submit() {
            when {
                mViewModel.todoTitle.get().isEmpty() -> {
                    showMessage("请填写标题")
                }
                mViewModel.todoContent.get().isEmpty() -> {
                    showMessage("请填写内容")
                }
                mViewModel.todoTime.get().isEmpty() -> {
                    showMessage("请选择预计完成时间")
                }
                else -> {
                    todoResponse.notNull({
                        showMessage("确认提交编辑吗？", positiveButtonText = "提交", positiveAction = {
                            mViewModel.updateTodo(
                                it.id,
                                mViewModel.todoTitle.get(),
                                mViewModel.todoContent.get(),
                                mViewModel.todoTime.get(),
                                TodoType.byValue(mViewModel.todoLeve.get()).type
                            )
                        }, negativeButtonText = "取消")
                    }, {
                        showMessage("确认添加吗？", positiveButtonText = "添加", positiveAction = {
                            mViewModel.addTodo(
                                mViewModel.todoTitle.get(),
                                mViewModel.todoContent.get(),
                                mViewModel.todoTime.get(),
                                TodoType.byValue(mViewModel.todoLeve.get()).type
                            )
                        }, negativeButtonText = "取消")
                    })
                }
            }
        }

    }
}