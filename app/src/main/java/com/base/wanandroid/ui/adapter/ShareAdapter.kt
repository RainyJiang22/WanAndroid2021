package com.base.wanandroid.ui.adapter

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.base.wanandroid.R
import com.base.wanandroid.data.ArticleResponse
import com.base.wanandroid.ui.share.ShareViewModel
import com.base.wanandroid.ui.web.WebActivity
import com.base.wanandroid.utils.RxTransformer
import com.base.wanandroid.utils.html2Spanned
import com.base.wanandroid.widget.Dialog
import com.base.wanandroid.widget.view.CollectView
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
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


    init {
        //设置默认加载动画
        setAnimationWithDefault(AnimationType.ScaleIn)
        //先注册需要点击的子控件id
        addChildClickViewIds(R.id.share_item, R.id.share_delete)
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
        }
    }
}