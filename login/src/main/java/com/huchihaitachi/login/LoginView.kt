package com.huchihaitachi.login

import com.huchihaitachi.base.BaseView
import io.reactivex.Observable

interface LoginView : BaseView<LoginViewState> {
  val loginIntent: Observable<Unit>
}