package com.huchihaitachi.loginwebview

import com.huchihaitachi.base.BaseViewState

data class LoginWebViewState(
  val loggedIn: Boolean,
  val error: Throwable?
) : BaseViewState