package com.huchihaitachi.loginwebview.presentation

import com.huchihaitachi.base.BasePresenter
import com.huchihaitachi.base.RxSchedulers
import com.huchihaitachi.loginwebview.di.scope.LoginWebScope
import com.huchihaitachi.loginwebview.presentation.coordination.RootTransaction
import com.huchihaitachi.usecase.LoginUseCase
import javax.inject.Inject

@LoginWebScope
class LoginWebPresenter @Inject constructor(
  private val loginUseCase: LoginUseCase,
  private val rootTransaction: RootTransaction,
  loginWebViewState: LoginWebViewState,
  rxSchedulers: RxSchedulers
) : BasePresenter<LoginWebView, LoginWebViewState>(
  loginWebViewState, rxSchedulers
) {

  override fun bindIntents() {
    view?.loginCodeIntent
      ?.observeOn(rxSchedulers.io)
      ?.flatMapCompletable { code -> loginUseCase(code) }
      ?.toSingleDefault(LoginWebViewState(true, null))
      ?.onErrorReturn { throwable -> LoginWebViewState(false, throwable) }
      ?.observeOn(rxSchedulers.ui)
      ?.subscribe { s ->
        state = s
        rootTransaction.viewAnime()
      }
      ?.let(disposables::add)
  }
}