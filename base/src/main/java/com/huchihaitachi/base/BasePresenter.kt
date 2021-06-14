package com.huchihaitachi.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

abstract class BasePresenter<V : BaseView<S>, S : BaseViewState>(
  initialState: S,
  protected val rxSchedulers: RxSchedulers
) {
  private val observableState: BehaviorSubject<S> = BehaviorSubject.create()
  protected var state: S = initialState.also(observableState::onNext)
    set(value) {
      field = value
      observableState.onNext(value)
    }
  protected val disposables: CompositeDisposable = CompositeDisposable()
  protected var view: V? = null

  fun bind(view: V) {
    this.view = view
    disposables.add(
      observableState.observeOn(rxSchedulers.ui)
        .subscribe { state ->
          view.render(state)
        }
    )
  }

  fun unbind() {
    this.view = null
    disposables.dispose()
  }

  abstract fun bindIntents()
}