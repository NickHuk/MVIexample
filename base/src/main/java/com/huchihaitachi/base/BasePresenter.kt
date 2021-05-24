package com.huchihaitachi.base

import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<V : BaseView<S>, S : BaseViewState>() {
  protected val disposables: CompositeDisposable = CompositeDisposable()
  protected var view: V? = null

  fun bind(view: V) {
    this.view = view
  }

  fun unbind() {
    this.view = null
    disposables.dispose() //??
  }

  abstract fun bindIntents()
}