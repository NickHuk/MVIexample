package com.huchihaitachi.base

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

abstract class BasePresenter<V : BaseView<S>, S : BaseViewState>(initialState: S) {
  protected var state: S = initialState
    set(value) {
      field = value
      observableState.onNext(value)
    }
  protected val disposables: CompositeDisposable = CompositeDisposable()
  protected var view: V? = null
  private val observableState: BehaviorSubject<S> = BehaviorSubject.createDefault(initialState)

  fun bind(view: V) {
    this.view = view
    disposables.add(
      observableState.observeOn(AndroidSchedulers.mainThread())
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