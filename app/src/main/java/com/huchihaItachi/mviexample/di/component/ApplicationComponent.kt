package com.huchihaItachi.mviexample.di.component

import android.content.Context
import com.huchihaItachi.mviexample.MVIApplication
import com.huchihaitachi.login.di.LoginSubcomponent
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

    fun loginSubcomponent(): LoginSubcomponent.Factory

    fun inject(application: MVIApplication)
}