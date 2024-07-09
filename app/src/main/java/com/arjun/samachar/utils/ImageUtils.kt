package com.arjun.samachar.utils

import android.widget.ImageView
import com.arjun.samachar.R
import com.bumptech.glide.Glide

fun ImageView.loadImage(url: String) {
    Glide.with(this).load(url).placeholder(R.color.grey).into(this)
}