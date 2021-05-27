package com.huchihaitachi.anilist.presentation

import com.huchihaitachi.base.BaseView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

interface AnilistView: BaseView<AnilistViewState> {
  val loadAnimePage: Observable<Unit>
  val reload: Observable<Unit>
}