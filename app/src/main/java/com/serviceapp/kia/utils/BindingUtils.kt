package com.serviceapp.kia.utils

import android.graphics.Paint
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import com.bumptech.glide.Glide
import com.serviceapp.kia.R
import de.hdodenhof.circleimageview.CircleImageView
import java.text.DecimalFormat

/*
 *Created by Adithya T Raj on 24-06-2021
*/

@BindingAdapter("glideImage")
fun loadImage(view: AppCompatImageView, url: String?) {
    Glide.with(view)
        .load(url)
        .placeholder(R.drawable.ic_placeholder)
        .into(view)
}

@BindingAdapter("glideCircleImage")
fun loadCircleImage(view: CircleImageView, url: String?) {
    Glide.with(view)
        .load(url)
        .placeholder(R.drawable.ic_placeholder)
        .into(view)
}

@BindingAdapter("byteImage")
fun bitMapImage(view: AppCompatImageView, encodedString: String?) {
    if (encodedString != null) {
        view.setImageBitmap(getImageBitMap(encodedString))
    }
}

@BindingAdapter("byteImageCircle")
fun bitMapCircleImage(view: CircleImageView, encodedString: String?) {
    if (encodedString != null) {
        view.setImageBitmap(getImageBitMap(encodedString))
    }
}

@BindingAdapter("customRating")
fun setCustomRating(view: AppCompatRatingBar, userRating: String?) {
    view.rating = if (userRating.isNullOrEmpty()) {
        0F
    } else {
        userRating.toFloat()
    }
}

@BindingAdapter("customText")
fun setCustomText(view: TextView, userText: String?) {
    val newText = try {
        DecimalFormat("##,##,##,##,###.##").format(userText?.toFloat())
    } catch (e: Exception) {
        userText
    }
    val price = "₹ $newText"
    view.text = price
}

@BindingAdapter("arrayList")
fun setArraySpinner(view:Spinner, liveData: LiveData<MutableList<String>>) {
    liveData.observeForever { arrayList ->
        val adapter = ArrayAdapter(view.context, android.R.layout.simple_spinner_item, arrayList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        view.adapter = adapter
    }

}

@BindingAdapter("strikeText")
fun setStrikeText(view: TextView, userText: String?) {
    view.visibility = if (userText.isNullOrEmpty()) {
        View.GONE
    } else {
        //val newText = String.format("%,d", userText.toInt())
        val newText = DecimalFormat("##,##,##,##,###.##").format(userText.toFloat())
        view.paintFlags = view.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        val price = " ₹$newText "
        view.text = price
        View.VISIBLE
    }
}

@BindingAdapter("bracketText")
fun setBracketText(view: TextView, userText: String?) {
    val newText = "($userText)"
    view.text = newText
}

@BindingAdapter("buttonVisibility")
fun setButtonVisibility(view: Button, userText: String?) {
    if (userText.equals("0")) {
        view.visibility = View.VISIBLE
    } else {
        view.visibility = View.GONE
    }
}

@BindingAdapter("layoutVisibility")
fun setLayoutVisibility(view: ConstraintLayout, userText: String?) {
    if (!userText.equals("0")) {
        view.visibility = View.VISIBLE
    } else {
        view.visibility = View.GONE
    }
}

@BindingAdapter("progressVisibility")
fun setProgressVisibility(view: ProgressBar, userText: String?) {
    if (!userText.equals("0")) {
        view.visibility = View.VISIBLE
    } else {
        view.visibility = View.GONE
    }
}

@BindingAdapter("buttonEnable")
fun setButtonEnable(view: Button, userText: String?) {
    view.isEnabled = userText.equals("0")
}

@BindingAdapter("spinnerList")
fun setNormalSpinner(view:Spinner, arrayList: MutableList<String>) {
    val adapter = ArrayAdapter(view.context, android.R.layout.simple_spinner_item, arrayList)
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    view.adapter = adapter
}

@BindingAdapter("cartCount")
fun setCartCount(view: TextView, userText: Int?) {
    view.text = "$userText"
}