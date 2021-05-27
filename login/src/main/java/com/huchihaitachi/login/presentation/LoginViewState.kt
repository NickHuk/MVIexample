package com.huchihaitachi.login.presentation

import com.huchihaitachi.base.BaseViewState

data class LoginViewState(
  val isLoading: Boolean = false,
  val error: Throwable? = null
) : BaseViewState