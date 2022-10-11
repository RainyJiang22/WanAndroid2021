package com.base.wanandroid.ui.history;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.base.wanandroid.history.bean.HistoryEntity;

/**
 * @author jiangshiyu
 * @date 2022/6/9
 */
public class HistoryRecordDiffCallBack extends DiffUtil.ItemCallback<HistoryEntity> {

    @Override
    public boolean areItemsTheSame(@NonNull HistoryEntity oldItem, @NonNull HistoryEntity newItem) {
        return oldItem.getTitle().equals(newItem.getTitle());
    }

    @Override
    public boolean areContentsTheSame(@NonNull HistoryEntity oldItem, @NonNull HistoryEntity newItem) {
        return oldItem.equals(newItem);
    }
}
