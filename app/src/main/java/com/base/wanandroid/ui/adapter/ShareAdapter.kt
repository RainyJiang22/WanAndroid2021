package com.base.wanandroid.ui.adapter

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.base.wanandroid.R
import com.base.wanandroid.bean.ArticleResponse
import com.base.wanandroid.ui.share.ShareViewModel
import com.base.wanandroid.ui.web.WebActivity
import com.base.wanandroid.utils.RxTransformer
import com.base.wanandroid.utils.html2Spanned
import com.base.wanandroid.widget.Dialog
import com.base.wanandroid.widget.view.CollectView
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.drake.channel.receiveTag
import kotlinx.coroutines.launch

/**
 * @author jiangshiyu
 * @date 2022/6/16
 */
class ShareAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val viewModel: ShareViewModel
) :
    BaseQuickAdapter<ArticleResponse, BaseViewHolder>(R.layout.item_share_list) {

    companion object {
        //item的位置
        private var index: Int = 0
    }

    init {
        //设置默认加载动画
        setAnimationWithDefault(AnimationType.ScaleIn)
        //先注册需要点击的子控件id
        addChildClickViewIds(R.id.share_item, R.id.share_delete)
        //设置子控件点击监听
        setOnItemChildClickListener { adapter, view, position ->
            //获取到对应的item数据
            val item = adapter.data[position] as ArticleResponse
            when (view.id) {
                R.id.share_item -> {
                    //跳转文章网页打开链接，传递文章id标题链接及收藏与否
                    item.run { WebActivity.start(context, id, title, link, collect) }
                    //跳转后将位置传递
                    index = position
                }
                R.id.share_delete -> {
                    //删除对应分享文章
                    Dialog.getConfirmDialog(
                        context,
                        context.getString(R.string.delete_share_confirm)
                    ) { _, _ ->
                        lifecycleOwner.lifecycleScope.launch {
                            viewModel.deleteSharedArticle(item.id)
                                .compose(RxTransformer.async())
                                .subscribe()
                        }
                        //adapter中删除
                        adapter.removeAt(position)
                        ToastUtils.showShort(context.getString(R.string.delete_succeed))
                    }.show()
                }
            }
        }
        //接收消息事件，同步收藏与否
        lifecycleOwner.receiveTag(true.toString(), false.toString()) {
            //将对应的数据类的收藏字段修改
            getItem(index).collect = it.toBoolean()
            //根据item的位置获取到它的收藏控件对象
            val collectView = getViewByPosition(index, R.id.item_share_collect) as CollectView
            //收藏控件是否选中
            collectView.isChecked = it.toBoolean()
        }
    }

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        super.onItemViewHolderCreated(viewHolder, viewType)
        viewHolder.getView<CollectView>(R.id.item_share_collect)
            .setOnClickListener(object : CollectView.OnClickListener {
                //收藏控件点击事件回调
                override fun onClick(v: CollectView) {
                    if (v.isChecked) {
                        //选中收藏文章
                        lifecycleOwner.lifecycleScope.launch {
                            viewModel.collectCurrentArticle(data[viewHolder.adapterPosition - headerLayoutCount].id)
                                .compose(RxTransformer.async())
                                .subscribe()
                        }
                    } else {
                        //未选中取消收藏文章
                        lifecycleOwner.lifecycleScope.launch {
                            viewModel.unCollectArticle(data[viewHolder.adapterPosition - headerLayoutCount].id)
                                .compose(RxTransformer.async())
                                .subscribe()
                        }
                    }
                    //收藏控件点击后，同步一下数据类，跳转网页同步收藏
                    data[viewHolder.adapterPosition - headerLayoutCount].collect = v.isChecked
                }
            })
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun convert(holder: BaseViewHolder, item: ArticleResponse) {
        item.run {
            //作者or分享人
            holder.setText(R.id.item_share_author, author.ifEmpty { shareUser })
            //发布日期
            holder.setText(R.id.item_share_date, niceDate)
            //文章标题
            holder.setText(R.id.item_share_title, title.html2Spanned())
            //文章章节
            holder.setText(
                R.id.item_share_chapter,
                ("$superChapterName·$chapterName").html2Spanned()
            )
            //是否收藏
            holder.getView<CollectView>(R.id.item_share_collect).setChecked(collect, false)
            //是否上新
            holder.setGone(R.id.item_share_new, (!fresh))
            //是否审核
            holder.setGone(R.id.item_share_review, audit != 0)
        }
    }
}