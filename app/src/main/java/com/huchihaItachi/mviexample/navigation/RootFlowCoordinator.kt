package com.huchihaItachi.mviexample.navigation

import android.util.Log
import com.huchihaitachi.base.di.scope.ActivityScope
import com.huchihaitachi.usecase.IsLoggedInUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@ActivityScope
class RootFlowCoordinator @Inject constructor(
  private val navigator: Navigator,
  private val loginFlowCoordinator: LoginFlowCoordinator,
  private val isLoggedInUseCase: IsLoggedInUseCase
) {
  private val disposables: CompositeDisposable = CompositeDisposable()

  init {
    disposables.add(
      isLoggedInUseCase()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
          { loggedIn ->
            if (loggedIn) {
              //TODO: start from anime list
            } else {
              loginFlowCoordinator.start()
            }
          },
          { error ->
            Log.d("debug", error.toString())
          }
        )

    )
  }
}