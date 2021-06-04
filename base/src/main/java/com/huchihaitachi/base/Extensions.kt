package com.huchihaitachi.base

import android.text.Spannable
import android.text.SpannableString

fun SpannableString.highlight(textToHighlight: String?, span: Any) =
  textToHighlight?.let { highlight ->
    this.setSpan(span, this.indexOf(highlight), highlight.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
  }
