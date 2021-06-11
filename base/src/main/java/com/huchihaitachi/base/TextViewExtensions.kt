package com.huchihaitachi.base

import android.content.res.Resources
import android.text.SpannableString
import android.widget.TextView
import androidx.annotation.StringRes

fun TextView.setTextAndHighlight(
  resources: Resources,
  span: Any,
  @StringRes textRes: Int,
  @StringRes highlightedPartRes: Int,
  vararg params: Any?
) {
  val highlightedPart = resources.getString(highlightedPartRes)
  (params.first()?.let { resources.getString(textRes, *params) } ?: highlightedPart)
    .let { titleText ->
      SpannableString(titleText).apply { highlight(highlightedPart, span) }
    }.let(::setText)
}