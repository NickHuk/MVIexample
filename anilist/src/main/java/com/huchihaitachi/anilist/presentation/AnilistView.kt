package com.huchihaitachi.anilist.presentation

import com.huchihaitachi.base.BaseView
import io.reactivex.Observable

interface AnilistView: BaseView<AnilistViewState> {
  val loadPage: Observable<Unit>
  val refresh: Observable<Unit>
  val showDetails: Observable<Int>
  val hideDetails: Observable<Unit>
}