package com.huchihaitachi.loginwebview.presentation

import com.huchihaitachi.base.BasePresenter
import com.huchihaitachi.loginwebview.di.scope.LoginWebScope
import com.huchihaitachi.loginwebview.presentation.coordination.RootTransaction
import com.huchihaitachi.usecase.LoginUseCase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

@LoginWebScope
class LoginWebPresenter @Inject constructor(
  private val loginUseCase: LoginUseCase,
  private val rootTransaction: RootTransaction
) : BasePresenter<LoginWebView, LoginWebViewState>(
  LoginWebViewState(false, null)
) {

  override fun bindIntents() {
    view?.loginCodeIntent
      ?.observeOn(Schedulers.io())
      ?.flatMapCompletable { code -> loginUseCase(code) }
      ?.toSingleDefault(LoginWebViewState(true, null))
      ?.onErrorReturn { throwable -> LoginWebViewState(false, throwable) }
      ?.observeOn(AndroidSchedulers.mainThread())
      ?.subscribe { s ->
        state = s
        rootTransaction.viewAnime()
      }
      ?.let(disposables::add)
  }
}