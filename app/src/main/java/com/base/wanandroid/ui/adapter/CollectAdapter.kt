package com.base.wanandroid.ui.adapter

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.base.wanandroid.R
import com.base.wanandroid.data.CollectResponse
import com.base.wanandroid.ui.web.WebActivity
import com.base.wanandroid.utils.html2Spanned
import com.base.wanandroid.utils.html2String
import com.base.wanandroid.widget.view.CollectView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.material.imageview.ShapeableImageView
import per.goweii.reveallayout.RevealLayout

/**
 * @author jiangshiyu
 * @date 2022/6/14
 * 收藏adapter
 */
class CollectAdapter(
    data: ArrayList<CollectResponse>
) : BaseQuickAdapter<CollectResponse, BaseViewHolder>(R.layout.item_collect_list, data) {

    private var collectAction: (item: CollectResponse, v: CollectView, position: Int) -> Unit =
        { _: CollectResponse, _: CollectView, _: Int -> }

    init {
        setAnimationWithDefault(AnimationType.AlphaIn)
        //设置Item点击事件
        this.setOnItemClickListener { _, _, position ->
            //跳转文章网页打开链接，传递文章id标题链接及收藏与否，外加一个收藏文章原始id，同时将数据类传递过去
            data[position].run {
                WebActivity.start(
                    context, id, title, link, true, originId = originId, data = this
                )
            }
        }
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


            holder.getView<CollectView>(R.id.item_article_collect)
                .setOnClickListener(object : CollectView.OnClickListener {
                    override fun onClick(v: CollectView) {
                        collectAction.invoke(item, v, holder.absoluteAdapterPosition)
                    }

                })
        }

    }

    fun setCollectClick(inputCollectAction: (item: CollectResponse, v: CollectView, position: Int) -> Unit) {
        this.collectAction = inputCollectAction
    }
}