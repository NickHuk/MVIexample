package com.huchihaitachi.login.presentation

import com.huchihaitachi.base.BasePresenter
import com.huchihaitachi.base.RxSchedulers
import com.huchihaitachi.login.di.LoginScope
import com.huchihaitachi.login.presentation.coordination.LoginTransaction
import javax.inject.Inject

@LoginScope
class LoginPresenter @Inject constructor(
  private val loginTransaction: LoginTransaction,
  loginViewState: LoginViewState,
  rxSchedulers: RxSchedulers
) : BasePresenter<LoginView, LoginViewState>(loginViewState, rxSchedulers) {

  //Square android
  override fun bindIntents() {
    view?.let { view ->
      view.loginIntent
        .map { LoginViewState(true, null) }
        //TODO: check internet connection and display an error if needed (enter error state for a few seconds)
        .subscribeOn(rxSchedulers.io)
        .observeOn(rxSchedulers.ui)
        .subscribe { s ->
          state = s
          loginTransaction.oauth()
        }
        .let(disposables::add)
    }
  }
}