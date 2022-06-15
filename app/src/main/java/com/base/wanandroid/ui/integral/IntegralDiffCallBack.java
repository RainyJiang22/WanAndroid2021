package com.base.wanandroid.ui.integral;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.base.wanandroid.bean.ArticleResponse;
import com.base.wanandroid.bean.IntegralResponse;

/**
 * @author jiangshiyu
 * @date 2022/5/18
 */
public class IntegralDiffCallBack extends DiffUtil.ItemCallback<IntegralResponse> {

    @Override
    public boolean areItemsTheSame(@NonNull IntegralResponse oldItem, @NonNull IntegralResponse newItem) {
        return oldItem.getUserName().equals(newItem.getUserName());
    }

    @Override
    public boolean areContentsTheSame(@NonNull IntegralResponse oldItem, @NonNull IntegralResponse newItem) {
        return oldItem.equals(newItem);
    }
}
