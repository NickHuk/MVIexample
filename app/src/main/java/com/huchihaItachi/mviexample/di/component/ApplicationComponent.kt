package com.huchihaItachi.mviexample.di.component

import android.content.Context
import com.huchihaItachi.database.di.DatabaseModule
import com.huchihaItachi.mviexample.MVIApplication
import com.huchihaItachi.mviexample.di.module.ApplicationModule
import com.huchihaItachi.mviexample.di.module.DataSourceModule
import com.huchihaItachi.mviexample.di.module.RepositoryModule
import com.huchihaItachi.mviexample.di.module.UseCaseModule
import com.huchihaitachi.remoteapi.di.RemoteAPIModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
  modules = [
    ApplicationModule::class,
    RemoteAPIModule::class,
    DataSourceModule::class,
    RepositoryModule::class,
    UseCaseModule::class,
    DatabaseModule::class
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