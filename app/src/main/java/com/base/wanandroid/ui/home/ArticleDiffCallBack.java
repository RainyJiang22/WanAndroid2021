package com.base.wanandroid.ui.home;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.base.wanandroid.bean.ArticleResponse;

/**
 * @author jiangshiyu
 * @date 2022/5/18
 */
public class ArticleDiffCallBack extends DiffUtil.ItemCallback<ArticleResponse> {

    @Override
    public boolean areItemsTheSame(@NonNull ArticleResponse oldItem, @NonNull ArticleResponse newItem) {
        return oldItem.getTitle().equals(newItem.getTitle());
    }

    @Override
    public boolean areContentsTheSame(@NonNull ArticleResponse oldItem, @NonNull ArticleResponse newItem) {
        return oldItem.equals(newItem);
    }
}
