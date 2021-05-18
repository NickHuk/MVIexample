package com.huchihaItachi.mviexample.di.component

import android.content.Context
import com.huchihaItachi.mviexample.MVIApplication
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [

    ]
)
interface ApplicationComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ApplicationComponent
    }

    fun inject(application: MVIApplication)
}