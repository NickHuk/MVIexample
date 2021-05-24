package com.huchihaItachi.mviexample

import android.app.Application
import com.huchihaItachi.mviexample.di.ActivitySubcomponentProvider
import com.huchihaItachi.mviexample.di.component.ActivitySubcomponent
import com.huchihaItachi.mviexample.di.component.ApplicationComponent
import com.huchihaItachi.mviexample.di.component.DaggerApplicationComponent

class MVIApplication : Application(),
  ActivitySubcomponentProvider {
  val applicationComponent: ApplicationComponent by lazy {
    DaggerApplicationComponent.factory().create(applicationContext)
  }

  override fun onCreate() {
    super.onCreate()
    applicationComponent.inject(this)
  }

  override fun provideActivitySubcomponentFactory(): ActivitySubcomponent.Factory =
    applicationComponent.activitySubcomponent()
}
