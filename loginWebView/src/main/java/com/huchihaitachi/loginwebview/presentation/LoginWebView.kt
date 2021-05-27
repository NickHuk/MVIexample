package com.huchihaitachi.loginwebview.presentation

import com.huchihaitachi.base.BaseView
import io.reactivex.Observable

interface LoginWebView : BaseView<LoginWebViewState> {
  val loginCodeIntent: Observable<String>
}