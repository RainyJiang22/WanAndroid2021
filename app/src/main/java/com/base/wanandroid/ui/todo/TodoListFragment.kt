package com.base.wanandroid.ui.todo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.bottomsheets.setPeekHeight
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.afollestad.materialdialogs.list.listItems
import com.base.wanandroid.R
import com.base.wanandroid.base.BaseFragment
import com.base.wanandroid.databinding.FragmentTodoListBinding
import com.base.wanandroid.ext.init
import com.base.wanandroid.ext.initFooter
import com.base.wanandroid.ext.loadListData
import com.base.wanandroid.ext.loadServiceInit
import com.base.wanandroid.ext.showEmpty
import com.base.wanandroid.ext.showLoading
import com.base.wanandroid.ui.adapter.TodoAdapter
import com.base.wanandroid.utils.initFloatBtn
import com.base.wanandroid.widget.recyclerview.SpaceItemDecoration
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ToastUtils
import com.kingja.loadsir.core.LoadService
import me.hgj.jetpackmvvm.ext.nav
import me.hgj.jetpackmvvm.ext.navigateAction

/**
 * @author jiangshiyu
 * @date 2022/10/21
 */
class TodoListFragment : BaseFragment<TodoViewModel, FragmentTodoListBinding>() {


    private val todoAdapter: TodoAdapter by lazy { TodoAdapter(arrayListOf()) }


    //界面管理器
    private lateinit var loadSir: LoadService<Any>


    @SuppressLint("CheckResult")
    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.titleBar.let {

            it.leftView.setOnClickListener {
                nav().navigateUp()
            }

            it.rightView.setOnClickListener {
                //添加待办todo界面
                nav().navigateAction(R.id.action_todoListFragment_to_addTodoFragment)
            }
        }


        loadSir = loadServiceInit(mViewBind.swipeRefresh) {
            loadSir.showLoading()
            mViewModel.getTodoData(true)
        }

        mViewBind.swipeRefresh.init {
            mViewModel.getTodoData(true)
        }

        mViewBind.recyclerView.init(LinearLayoutManager(context), todoAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
            it.initFooter {
                mViewModel.getTodoData(false)
            }
            it.initFloatBtn(mViewBind.fab)
        }

        todoAdapter.run {
            setOnItemClickListener { _, _, position ->
                //添加待办todo列表
                nav().navigateAction(R.id.action_todoListFragment_to_addTodoFragment,
                    Bundle().apply {
                        putParcelable("todo", todoAdapter.data[position])
                    })
            }

            addChildClickViewIds(R.id.item_todo_setting)
            setOnItemChildClickListener { adapter, view, position ->
                when (view.id) {
                    R.id.item_todo_setting -> {
                        val items = if (todoAdapter.data[position].isDone()) {
                            listOf("删除", "编辑")
                        } else {
                            listOf("删除", "编辑", "完成")
                        }
                        activity?.let { activity ->
                            MaterialDialog(activity, BottomSheet())
                                .lifecycleOwner(viewLifecycleOwner).show {
                                    cornerRadius(8f)
                                    setPeekHeight(ConvertUtils.dp2px((items.size * 50 + 36).toFloat()))
                                    listItems(items = items) { _, index, item ->
                                        when (index) {
                                            0 -> {
                                                //删除
                                                mViewModel.delTodo(
                                                    todoAdapter.data[position].id,
                                                    position
                                                )
                                            }
                                            1 -> {
                                                //编辑
                                                nav().navigateAction(R.id.action_todoListFragment_to_addTodoFragment,
                                                    Bundle().apply {
                                                        putParcelable(
                                                            "todo",
                                                            todoAdapter.data[position]
                                                        )
                                                    })

                                            }
                                            2 -> {
                                                //完成
                                                mViewModel.doneTodo(
                                                    todoAdapter.data[position].id,
                                                    position
                                                )
                                            }
                                        }
                                    }
                                }
                        }

                    }
                }
            }
        }
    }

    override fun lazyLoadData() {
        loadSir.showLoading()
        mViewModel.getTodoData(true)
    }


    override fun createObserver() {
        mViewModel.todoDataState.observe(viewLifecycleOwner) {
            loadListData(it, todoAdapter, loadSir, mViewBind.recyclerView, mViewBind.swipeRefresh)
        }

        //删除操作
        mViewModel.delDataState.observe(viewLifecycleOwner) {
            if (it.isSuccess) {
                if (todoAdapter.data.size == 1) {
                    loadSir.showEmpty()
                }
                todoAdapter.remove(it.data!!)
            } else {
                ToastUtils.showShort(it.errorMsg)
            }
        }

        //完成操作
        mViewModel.doneDataState.observe(viewLifecycleOwner) {
            if (it.isSuccess) {
                mViewBind.swipeRefresh.isRefreshing = true
                mViewModel.getTodoData(true)
            } else {
                ToastUtils.showShort(it.errorMsg)
            }
        }


    }
}