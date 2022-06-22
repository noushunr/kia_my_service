package com.serviceapp.kia.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.serviceapp.kia.R
import com.serviceapp.kia.data.model.SlideModel
import com.smarteist.autoimageslider.SliderViewAdapter
import kotlinx.android.synthetic.main.image_slider_layout_item.view.*

/*
 *Created by Adithya T Raj on 30-06-2021
*/

class SliderAdapter(
    val items: MutableList<SlideModel>, val context: Context
    ) : SliderViewAdapter<SliderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?): ViewHolder {
        return ViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.image_slider_layout_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val requestOptions = RequestOptions()
            .placeholder(R.drawable.ic_placeholder)
            .error(R.drawable.ic_placeholder)

        if (items[position].imagePath != 0 && items[position].imagePath != null) {
            Glide.with(context)
                .load(items[position].imagePath)
                .apply(requestOptions)
                .into(holder.imageViewBackground)
        } else {
            Glide.with(context)
                .load(items[position].imageUrl)
                .apply(requestOptions)
                .into(holder.imageViewBackground)
        }

    }

    override fun getCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View)
        : SliderViewAdapter.ViewHolder(itemView) {

        val imageViewBackground: ImageView = itemView.iv_auto_image_slider
        val imageGifContainer: ImageView = itemView.iv_gif_container
        val textViewDescription: TextView = itemView.tv_auto_image_slider

    }

}