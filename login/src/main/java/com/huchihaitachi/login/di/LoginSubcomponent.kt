package com.huchihaitachi.login.di

import com.huchihaitachi.login.LoginController
import dagger.Subcomponent

@LoginScope
@Subcomponent(
    modules = [
        LoginModule::class
    ]
)
interface LoginSubcomponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): LoginSubcomponent
    }

    fun inject(loginController: LoginController)
}