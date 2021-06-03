package com.huchihaitachi.base

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RxSchedulers @Inject constructor() {

  val io: Scheduler
    get() = Schedulers.io()

  val ui: Scheduler
    get() = AndroidSchedulers.mainThread()
}