package com.base.wanandroid.ui.integral

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.base.wanandroid.bean.CoinInfoListResponse
import com.base.wanandroid.bean.IntegralListResponse
import com.base.wanandroid.network.RetrofitHelper
import com.base.wanandroid.viewmodel.request.RequestIntegralViewModel
import io.reactivex.Observable
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * @author jiangshiyu
 * @date 2022/6/15
 */
class IntegralViewModel: RequestIntegralViewModel() {


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