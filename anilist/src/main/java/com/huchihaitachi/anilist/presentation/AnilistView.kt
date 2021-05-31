package com.huchihaitachi.anilist.presentation

import com.huchihaitachi.base.BaseView
import com.huchihaitachi.domain.Anime
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

interface AnilistView: BaseView<AnilistViewState> {
  val loadAnimePage: Observable<Unit>
  val reload: Observable<Unit>
  val showDetails: Observable<Int>
  val hideDetails: Observable<Unit>
}