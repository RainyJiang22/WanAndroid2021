package com.base.wanandroid.ui.integral

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.base.wanandroid.bean.CoinInfoListResponse
import com.base.wanandroid.bean.IntegralListResponse
import com.base.wanandroid.network.RetrofitHelper
import io.reactivex.Observable

/**
 * @author jiangshiyu
 * @date 2022/6/15
 */
class IntegralViewModel(application: Application) : AndroidViewModel(application) {


    /**
     * 我的积分列表
     */
    fun getIntegralList(page: Int): Observable<IntegralListResponse> {
        return RetrofitHelper.get().integralList(page)
    }

    /**
     * 积分排行列表
     */
    fun getLeaderBoardList(page: Int): Observable<CoinInfoListResponse> {
        return RetrofitHelper.get().leaderboardList(page)
    }
}