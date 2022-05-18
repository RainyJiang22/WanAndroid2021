package com.base.wanandroid.ui.adapter

import android.os.Build
import androidx.annotation.RequiresApi
import com.base.wanandroid.R
import com.base.wanandroid.bean.ArticleResponse
import com.base.wanandroid.utils.html2Spanned
import com.base.wanandroid.utils.html2String
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.material.imageview.ShapeableImageView


/**
 * @author jiangshiyu
 * @date 2022/3/9
 */
class ArticleAdapter(private val showTag: Boolean = false) :
    BaseQuickAdapter<ArticleResponse, BaseViewHolder>(R.layout.item_article_list) {


    @RequiresApi(Build.VERSION_CODES.N)
    override fun convert(holder: BaseViewHolder, item: ArticleResponse) {
        item.apply {

            //作者or分享人
            holder.setText(R.id.item_article_author, author.ifEmpty { shareUser })

            //发布日期
            holder.setText(R.id.item_article_date, niceDate)
            //文章标题
            holder.setText(R.id.item_article_title, title.html2Spanned())
            //文章章节
            holder.setText(
                R.id.item_article_chapter,
                ("$superChapterName·$chapterName").html2Spanned()
            )

            //是否置顶
            holder.setGone(R.id.item_article_top, (type != 1))
            //是否上新
            holder.setGone(R.id.item_article_new, (!fresh))
            //显示标签
            if (showTag) {
                //是否标签
                holder.setGone(R.id.item_article_tag, tags.isNullOrEmpty())
                if (tags.isNotEmpty()) {
                    //标签文字
                    holder.setText(R.id.item_article_tag, tags[0].name)
                }
            } else {
                //隐藏标签
                holder.setGone(R.id.item_article_tag, true)
            }
            //项目图片
            holder.setGone(R.id.item_article_image, envelopePic.isEmpty())
            if (envelopePic.isNotEmpty()) {
                //加载图片
                holder.getView<ShapeableImageView>(R.id.item_article_image).run {
                    Glide.with(context)
                        .load(envelopePic)
                        .placeholder(R.drawable.bg_project)
                        .transition(DrawableTransitionOptions.withCrossFade(500))
                        .into(this)
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