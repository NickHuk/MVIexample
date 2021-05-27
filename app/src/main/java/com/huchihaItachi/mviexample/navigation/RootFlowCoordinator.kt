package com.huchihaItachi.mviexample.navigation

import android.util.Log
import com.huchihaitachi.base.di.scope.ActivityScope
import com.huchihaitachi.loginwebview.presentation.coordination.RootTransaction
import com.huchihaitachi.usecase.IsLoggedInUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@ActivityScope
class RootFlowCoordinator @Inject constructor(
  private val navigator: Navigator,
  private val loginFlowCoordinator: LoginFlowCoordinator,
  private val animeFlowCoordinator: AnimeFlowCoordinator,
  private val isLoggedInUseCase: IsLoggedInUseCase
) : RootTransaction {
  private val disposables: CompositeDisposable = CompositeDisposable()

  init {
    disposables.add(
      isLoggedInUseCase()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
          { loggedIn ->
            if (loggedIn) {
              animeFlowCoordinator.start()
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

  override fun viewAnime() {
    animeFlowCoordinator.start()
  }
}