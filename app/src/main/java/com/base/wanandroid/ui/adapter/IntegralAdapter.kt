package com.base.wanandroid.ui.adapter

import android.os.Build
import androidx.annotation.RequiresApi
import com.base.wanandroid.R
import com.base.wanandroid.data.IntegralHistoryResponse
import com.base.wanandroid.utils.html2Spanned
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @author jiangshiyu
 * @date 2022/6/15
 */
class IntegralAdapter :
    BaseQuickAdapter<IntegralHistoryResponse, BaseViewHolder>(R.layout.item_integral_list) {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun convert(holder: BaseViewHolder, item: IntegralHistoryResponse) {
        holder.setText(R.id.tv_reason, item.reason)
            .setText(R.id.tv_desc, item.desc.html2Spanned())
            .setText(R.id.tv_score, "+${item.coinCount}")
    }
}