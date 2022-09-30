package com.base.wanandroid.ui.integral;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import com.base.wanandroid.data.IntegralHistoryResponse;

/**
 * @author jiangshiyu
 * @date 2022/5/18
 */
public class IntegralDiffCallBack extends DiffUtil.ItemCallback<IntegralHistoryResponse> {

    @Override
    public boolean areItemsTheSame(@NonNull IntegralHistoryResponse oldItem, @NonNull IntegralHistoryResponse newItem) {
        return oldItem.getUserName().equals(newItem.getUserName());
    }

    @Override
    public boolean areContentsTheSame(@NonNull IntegralHistoryResponse oldItem, @NonNull IntegralHistoryResponse newItem) {
        return oldItem.equals(newItem);
    }
}
