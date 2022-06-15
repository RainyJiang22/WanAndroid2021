package com.base.wanandroid.ui.adapter

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.base.wanandroid.R
import com.base.wanandroid.bean.CollectResponse
import com.base.wanandroid.ui.collect.CollectViewModel
import com.base.wanandroid.ui.web.WebActivity
import com.base.wanandroid.utils.RxTransformer
import com.base.wanandroid.utils.html2Spanned
import com.base.wanandroid.utils.html2String
import com.base.wanandroid.widget.view.CollectView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.drake.channel.receiveEvent
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import per.goweii.reveallayout.RevealLayout

/**
 * @author jiangshiyu
 * @date 2022/6/14
 * 收藏adapter
 */
class CollectAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val viewModel: CollectViewModel
) :
    BaseQuickAdapter<CollectResponse, BaseViewHolder>(R.layout.item_collect_list) {
    init {
        setAnimationWithDefault(AnimationType.AlphaIn)
        //设置Item点击事件
        this.setOnItemClickListener { _, _, position ->
            //跳转文章网页打开链接，传递文章id标题链接及收藏与否，外加一个收藏文章原始id，同时将数据类传递过去
            data[position].run {
                WebActivity.start(
                    context,
                    id,
                    title,
                    link,
                    true,
                    originId = originId,
                    data = this
                )
            }
        }
        //接收消息事件，同步收藏列表(从adapter移除对应数据)，默认自动在ON_DESTROY生命周期取消接收
        lifecycleOwner.receiveEvent<CollectResponse> { remove(it) }
    }


    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        super.onItemViewHolderCreated(viewHolder, viewType)
        viewHolder.getView<CollectView>(R.id.item_article_collect)
            .setOnClickListener(object : CollectView.OnClickListener {
                //收藏控件点击事件回调
                override fun onClick(v: CollectView) {
                    if (!v.isChecked) {
                        //收藏页面默认全部选中，点击后直接取消收藏，并移除item
                        lifecycleOwner.lifecycleScope.launch {
                            viewModel.userUnCollectArticle(
                                data[viewHolder.absoluteAdapterPosition].id,
                                data[viewHolder.absoluteAdapterPosition].originId
                            )
                                .compose(RxTransformer.async())
                                .subscribe()
                            delay(300)
                            removeAt(viewHolder.absoluteAdapterPosition)
                        }
                    }
                }
            })
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun convert(holder: BaseViewHolder, item: CollectResponse) {
        item.run {
            //作者
            holder.setText(
                R.id.item_article_author,
                author.ifEmpty { context.getString(R.string.anonymous) })
            //发布日期
            holder.setText(R.id.item_article_date, niceDate)
            //文章标题
            holder.setText(R.id.item_article_title, title.html2Spanned())
            //文章章节
            holder.setText(R.id.item_article_chapter, (chapterName).html2Spanned())
            //全部收藏
            holder.getView<RevealLayout>(R.id.item_article_collect).setChecked(true, false)
            //项目图片
            holder.setGone(R.id.item_article_image, envelopePic.isEmpty())
            if (envelopePic.isNotEmpty()) {
                //加载图片
                holder.getView<ShapeableImageView>(R.id.item_article_image).run {
                    Glide.with(context).load(envelopePic).placeholder(R.drawable.bg_project)
                        .transition(DrawableTransitionOptions.withCrossFade(500)).into(this)
                }
            }
            //项目内容
            holder.setGone(R.id.item_article_content, desc.isEmpty())
            if (desc.isNotEmpty()) {
                //内容描述
                holder.setText(R.id.item_article_content, desc.html2String())
            }
        }

    }
}