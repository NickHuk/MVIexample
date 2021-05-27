package com.huchihaitachi.base

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.setUrl(url: String) =
  Glide.with(this).load(url).into(this)
