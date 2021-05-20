package com.huchihaItachi.mviexample

import android.app.Application
import com.huchihaItachi.mviexample.di.component.ApplicationComponent
import com.huchihaItachi.mviexample.di.component.DaggerApplicationComponent
import com.huchihaitachi.login.di.LoginSubcomponent
import com.huchihaitachi.login.di.LoginSubcomponentProvider

class MVIApplication : Application(),
        LoginSubcomponentProvider
{
    val applicationComponent: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory().create(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()
        applicationComponent.inject(this)
    }

    override fun provideLoginSubcomponent(): LoginSubcomponent =
        applicationComponent.loginSubcomponent().create()
}
