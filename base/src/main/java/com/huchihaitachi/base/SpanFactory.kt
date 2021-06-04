package com.huchihaitachi.base

import android.graphics.Typeface
import android.text.style.StyleSpan
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpanFactory @Inject constructor() {

  fun createBold() = StyleSpan(Typeface.BOLD)
}