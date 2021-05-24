package com.huchihaItachi.mviexample.di.component

import android.content.Context
import com.huchihaItachi.mviexample.MVIApplication
import com.huchihaItachi.mviexample.di.module.ApplicationModule
import com.huchihaItachi.mviexample.di.module.DataSourceModule
import com.huchihaItachi.mviexample.di.module.RepositoryModule
import com.huchihaItachi.mviexample.di.module.UseCaseModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
  modules = [
    ApplicationModule::class,
    DataSourceModule::class,
    RepositoryModule::class,
    UseCaseModule::class
  ]
)
interface ApplicationComponent {

  @Component.Factory
  interface Factory {
    fun create(@BindsInstance context: Context): ApplicationComponent
  }

  fun activitySubcomponent(): ActivitySubcomponent.Factory

  fun inject(application: MVIApplication)
}