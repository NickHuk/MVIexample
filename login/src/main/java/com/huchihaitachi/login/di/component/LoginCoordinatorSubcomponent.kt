package com.huchihaitachi.login.di.component

import com.huchihaitachi.base.di.scope.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent
interface LoginCoordinatorSubcomponent {

  @Subcomponent.Factory
  interface Factory {
    fun create(): LoginCoordinatorSubcomponent
  }
}