package com.base.wanandroid.ui.adapter

import com.base.wanandroid.R
import com.base.wanandroid.data.IntegralResponse
import com.base.wanandroid.utils.randomColor
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @author jiangshiyu
 * @date 2022/6/15
 */
class LeaderBoardAdapter :
    BaseQuickAdapter<IntegralResponse, BaseViewHolder>(R.layout.item_leaderboard_list) {

    override fun convert(holder: BaseViewHolder, item: IntegralResponse) {
        holder.setText(R.id.item_integral_rank, item.rank)
            .setText(R.id.item_integral_name, item.username)
            .setText(R.id.item_integral_count, item.coinCount.toString())
    }
}