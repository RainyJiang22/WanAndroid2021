package com.base.wanandroid.ui.adapter

import com.base.wanandroid.R
import com.base.wanandroid.bean.CoinInfoResponse
import com.base.wanandroid.utils.randomColor
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @author jiangshiyu
 * @date 2022/6/15
 */
class LeaderBoardAdapter :
    BaseQuickAdapter<CoinInfoResponse, BaseViewHolder>(R.layout.item_leaderboard_list) {

    override fun convert(holder: BaseViewHolder, item: CoinInfoResponse) {
        holder.setText(R.id.item_integral_rank, item.rank)
            .setText(R.id.item_integral_lv, context.getString(R.string.integral_lv, item.level))
            .setTextColor(R.id.item_integral_lv, randomColor())
            .setText(R.id.item_integral_name, item.username)
            .setText(R.id.item_integral_count, item.coinCount.toString())
    }
}