package com.huchihaitachi.login

import com.huchihaitachi.base.BasePresenter
import com.huchihaitachi.base.OAUTH
import com.huchihaitachi.login.di.LoginScope
import com.huchihaitachi.usecase.LoginUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Named

@LoginScope
class LoginPresenter @Inject constructor(
  private val loginUseCase: LoginUseCase,
  @Named(OAUTH) var oauth: () -> Unit
) : BasePresenter<LoginView, LoginViewState>() {
  val loginViewState: BehaviorSubject<LoginViewState> = BehaviorSubject.create()

  init {
    loginViewState.onNext(LoginViewState.ShowLogin())
  }

  //Square android
  override fun bindIntents() {
    view?.loginIntent
      ?.map<LoginViewState> {
        LoginViewState.Loading()
      }
      ?.subscribeOn(Schedulers.io())
      ?.observeOn(AndroidSchedulers.mainThread())
      ?.subscribe(
        { state ->
          view?.render(state)
          oauth()
        }
      )?.let(disposables::add)
  }
}