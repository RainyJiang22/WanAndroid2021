package com.base.wanandroid.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.base.wanandroid.R
import com.base.wanandroid.bean.BannerData
import com.base.wanandroid.ui.web.WebActivity
import com.bumptech.glide.RequestManager
import com.youth.banner.adapter.BannerAdapter

/**
 * @author jiangshiyu
 * @date 2022/3/8
 */
class ImageTitleAdapter(
    val context: Context,
    val glide: RequestManager,
    dataList: List<BannerData>
) :
    BannerAdapter<BannerData, ImageTitleAdapter.ImageTittleHolder>(dataList) {


    inner class ImageTittleHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val imageView: AppCompatImageView = view.findViewById(R.id.image)
        val title: AppCompatTextView = view.findViewById(R.id.bannerTitle)
    }

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): ImageTittleHolder {
        return ImageTittleHolder(
            LayoutInflater.from(parent?.context).inflate(R.layout.banner_image_title, parent, false)
        )
    }

    override fun onBindView(
        holder: ImageTittleHolder?,
        data: BannerData?,
        position: Int,
        size: Int
    ) {

        holder?.imageView?.let {
            glide.load(data?.imagePath).into(it)
        }
        holder?.title?.text = data?.title

        setOnBannerListener { mData, _ ->
            WebActivity.start(context, mData.url)
        }
    }
}