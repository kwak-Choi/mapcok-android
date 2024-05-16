package com.mapcok.ui.base

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@BindingAdapter("imageDrawable")
fun bindImageFromRes(view: ImageView, src:String?){
    val packageName = view.context.packageName
    src?.let {
        val redId = view.resources.getIdentifier(it,"drawable",packageName)
        view.setImageResource(redId)

    }
}

@BindingAdapter("setSimpleTime")
fun setHour(textView: TextView, date: Long) {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val toDate = sdf.format(Date(date))
    textView.text = toDate
}