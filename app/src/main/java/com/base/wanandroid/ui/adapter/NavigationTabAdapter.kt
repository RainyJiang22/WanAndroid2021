package com.base.wanandroid.ui.adapter

import androidx.core.content.ContextCompat
import com.base.wanandroid.R
import com.base.wanandroid.application.WanAndroidApplication
import com.base.wanandroid.data.NavigationResponse
import q.rorbin.verticaltablayout.adapter.TabAdapter
import q.rorbin.verticaltablayout.widget.ITabView

/**
 * @author jiangshiyu
 * @date 2022/6/7
 * 导航标签适配器
 */
class NavigationTabAdapter(private val dataList: List<NavigationResponse>) : TabAdapter {
    override fun getCount(): Int {
        return dataList.size
    }

    override fun getBadge(position: Int): ITabView.TabBadge? = null

    override fun getIcon(position: Int): ITabView.TabIcon? = null

    override fun getTitle(position: Int): ITabView.TabTitle {
        return ITabView.TabTitle.Builder()
            .setContent(dataList[position].name)
            .setTextColor(
                ContextCompat.getColor(
                    WanAndroidApplication.getApplication()!!,
                    R.color.color_vertical_tab_layout_text
                ), ContextCompat.getColor(WanAndroidApplication.getApplication()!!, R.color.gray_8f)
            )
            .build()
    }

    override fun getBackground(position: Int): Int = -1


}