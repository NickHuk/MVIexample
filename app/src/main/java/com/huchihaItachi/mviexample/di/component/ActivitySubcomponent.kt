package com.huchihaItachi.mviexample.di.component

import com.huchihaItachi.mviexample.MainActivity
import com.huchihaItachi.mviexample.di.module.CoordinatorModule
import com.huchihaitachi.anilist.di.component.AnilistSubcomponent
import com.huchihaitachi.base.di.scope.ActivityScope
import com.huchihaitachi.login.di.component.LoginSubcomponent
import com.huchihaitachi.loginwebview.di.component.LoginWebSubcomponent
import dagger.Subcomponent

@ActivityScope
@Subcomponent(
    modules = [
        CoordinatorModule::class
    ]
)
interface ActivitySubcomponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): ActivitySubcomponent
    }

    fun loginSubcomponent(): LoginSubcomponent.Factory

    fun loginWebSubcomponent(): LoginWebSubcomponent.Factory

    fun anilistSubcomponent(): AnilistSubcomponent.Factory

    fun inject(mainActivity: MainActivity)
}