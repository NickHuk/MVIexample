package com.huchihaitachi.login

import com.huchihaitachi.base.BasePresenter
import com.huchihaitachi.usecase.LoginUseCase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LoginPresenter @Inject constructor(
    private val loginUseCase: LoginUseCase
) : BasePresenter<LoginView, LoginViewState>() {
    val loginViewState: BehaviorSubject<LoginViewState> = BehaviorSubject.create()

    init {
        loginViewState.onNext(LoginViewState.ShowLogin())
    }

    override fun bindIntents() {
        view?.loginIntent
            ?.flatMap<LoginViewState> {
                loginUseCase().map { LoginViewState.Success() }
            }?.startWith(LoginViewState.Loading())
            ?.onErrorReturn { error -> LoginViewState.Error(error) }
            ?.switchMap { state ->
                if(state is LoginViewState.Error) {
                    Observable.timer(2, TimeUnit.SECONDS)
                        .map<LoginViewState> { LoginViewState.ShowLogin() }
                        .startWith(state)
                } else {
                    Observable.just(state)
                }
            }
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                { state -> view?.render(state) }
            )
    }


}