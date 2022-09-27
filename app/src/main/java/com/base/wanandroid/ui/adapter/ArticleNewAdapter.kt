package com.base.wanandroid.ui.adapter

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleOwner
import com.base.wanandroid.R
import com.base.wanandroid.bean.NoDataResponse
import com.base.wanandroid.data.ArticleResponse
import com.base.wanandroid.ui.author.AuthorActivity
import com.base.wanandroid.ui.web.WebActivity
import com.base.wanandroid.utils.html2Spanned
import com.base.wanandroid.utils.html2String
import com.base.wanandroid.widget.view.CollectView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.drake.net.Post
import com.drake.net.utils.scopeNetLife
import com.drake.serialize.intent.openActivity
import com.google.android.material.imageview.ShapeableImageView


/**
 * @author jiangshiyu
 * @date 2022/3/9
 */
class ArticleNewAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val showTag: Boolean = false
) :
    BaseQuickAdapter<ArticleResponse, BaseViewHolder>(R.layout.item_article_list) {

    companion object {
        private var index: Int = 0
    }

    init {
        //设置默认加载动画
        setAnimationWithDefault(AnimationType.ScaleIn)
        //设置Item点击事件
        this.setOnItemClickListener { _, _, position ->
            //跳转文章网页打开链接，传递文章id标题链接及收藏与否
            data[position].run { WebActivity.start(context, id, title, link, collect) }
            //跳转后将位置传递
            index = position
        }
        //先注册需要点击的子控件id
        this.addChildClickViewIds(R.id.item_article_author)
        //设置子控件点击监听
        this.setOnItemChildClickListener { _, view, position ->
            when (view.id) {
                R.id.item_article_author -> {
                    //打开文章作者页面
                    context.openActivity<AuthorActivity>(
                        //传递name和userId
                        "name" to data[position].run { author.ifEmpty { shareUser } },
                        "userId" to data[position].run { userId }
                    )
                }
            }
        }
    }


    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        super.onItemViewHolderCreated(viewHolder, viewType)
        // 收藏逻辑
        viewHolder.getView<CollectView>(R.id.item_article_collect)
            .setOnClickListener(object : CollectView.OnClickListener {
                override fun onClick(v: CollectView) {
                    if (v.isChecked) {
                        lifecycleOwner.scopeNetLife {
                            Post<NoDataResponse>("/lg/collect/${data[viewHolder.adapterPosition - headerLayoutCount].id}/json").await()
                        }
                    } else {
                        lifecycleOwner.scopeNetLife {
                            Post<NoDataResponse>("/lg/uncollect_originId/${data[viewHolder.adapterPosition - headerLayoutCount].id}/json").await()
                        }
                    }
                    data[viewHolder.adapterPosition - headerLayoutCount].collect =
                        v.isChecked
                }

            })

    }

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