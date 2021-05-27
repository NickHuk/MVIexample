package com.huchihaitachi.login.presentation

import com.huchihaitachi.base.BasePresenter
import com.huchihaitachi.login.di.LoginScope
import com.huchihaitachi.login.presentation.coordination.LoginTransaction
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

@LoginScope
class LoginPresenter @Inject constructor(
  private val loginTransaction: LoginTransaction
) : BasePresenter<LoginView, LoginViewState>(
  LoginViewState(false, null)
) {

  //Square android
  override fun bindIntents() {
    view?.let { view ->
      view.loginIntent
        .map { LoginViewState(true, null) }
        //TODO: check internet connection and display an error if needed (enter error state for a few seconds)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { s ->
          state = s
          loginTransaction.oauth()
        }
        .let(disposables::add)
    }
  }
}