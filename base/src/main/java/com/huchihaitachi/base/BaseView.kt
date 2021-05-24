package com.huchihaitachi.base

interface BaseView<in S : BaseViewState> {

  fun render(state: S)
}