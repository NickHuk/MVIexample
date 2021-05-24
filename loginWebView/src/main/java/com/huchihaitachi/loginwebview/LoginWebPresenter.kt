package com.huchihaitachi.loginwebview

import com.huchihaitachi.base.BasePresenter
import com.huchihaitachi.loginwebview.di.scope.LoginWebScope
import com.huchihaitachi.usecase.LoginUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@LoginWebScope
class LoginWebPresenter @Inject constructor(
  private val loginUseCase: LoginUseCase,

) : BasePresenter<LoginWebView, LoginWebViewState>() {

  override fun bindIntents() {
    view?.loginCodeIntent?.flatMapCompletable { code ->
      loginUseCase(code)
    }
      ?.toSingleDefault(LoginWebViewState(true, null))
      ?.onErrorReturn { throwable -> LoginWebViewState(false, throwable) }
      ?.subscribeOn(Schedulers.io())
      ?.observeOn(AndroidSchedulers.mainThread())
      ?.subscribe(
        { state ->

        }
      )
  }
}