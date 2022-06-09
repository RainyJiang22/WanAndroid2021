package com.base.wanandroid.ui.adapter

import android.os.Build
import android.text.format.DateFormat
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView
import com.base.wanandroid.R
import com.base.wanandroid.history.bean.HistoryEntity
import com.base.wanandroid.utils.html2Spanned
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import java.util.*

/**
 * @author jiangshiyu
 * @date 2022/6/9
 */
class HistoryRecordAdapter :
    BaseQuickAdapter<HistoryEntity, BaseViewHolder>(R.layout.item_history_record_list) {

    init {
        setAnimationWithDefault(AnimationType.ScaleIn)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun convert(holder: BaseViewHolder, item: HistoryEntity) {


        holder.getView<AppCompatTextView>(R.id.item_history_title)
            .text = item.title.html2Spanned()
        holder.getView<AppCompatTextView>(R.id.item_history_date)
            .text = item.date

    }
}