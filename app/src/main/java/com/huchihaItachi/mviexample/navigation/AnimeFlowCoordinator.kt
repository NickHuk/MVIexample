package com.huchihaItachi.mviexample.navigation

import com.huchihaitachi.base.di.scope.ActivityScope
import javax.inject.Inject

@ActivityScope
class AnimeFlowCoordinator @Inject constructor(
  private val navigator: Navigator
) {

  fun start() {
    navigator.openAnimeList()
  }
}