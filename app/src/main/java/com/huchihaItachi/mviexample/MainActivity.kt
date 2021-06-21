package com.huchihaItachi.mviexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.RouterTransaction
import com.huchihaItachi.mviexample.databinding.ActivityMainBinding
import com.huchihaItachi.mviexample.di.ActivitySubcomponentProvider
import com.huchihaItachi.mviexample.di.component.ActivitySubcomponent
import com.huchihaItachi.mviexample.navigation.Navigator
import com.huchihaItachi.mviexample.navigation.RootFlowCoordinator
import com.huchihaitachi.anilist.di.AnilistSubcomponentProvider
import com.huchihaitachi.anilist.di.component.AnilistSubcomponent
import com.huchihaitachi.login.di.LoginSubcomponentProvider
import com.huchihaitachi.login.di.component.LoginSubcomponent
import com.huchihaitachi.login.presentation.LoginController
import com.huchihaitachi.loginwebview.di.LoginWebSubcomponentProvider
import com.huchihaitachi.loginwebview.di.component.LoginWebSubcomponent
import javax.inject.Inject

class MainActivity : AppCompatActivity(),
  LoginSubcomponentProvider,
  LoginWebSubcomponentProvider,
  AnilistSubcomponentProvider {
  @Inject lateinit var rootFlowCoordinator: RootFlowCoordinator
  @Inject lateinit var navigator: Navigator
  private lateinit var activitySubcomponent: ActivitySubcomponent
  private lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    activitySubcomponent = (application as ActivitySubcomponentProvider)
      .provideActivitySubcomponentFactory().create()
    activitySubcomponent.inject(this)
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)
    navigator.router = Conductor.attachRouter(this, binding.controllerContainer, savedInstanceState)
    navigator.router.setRoot(RouterTransaction.with(LoginController()))
  }

  override fun provideLoginSubcomponent(): LoginSubcomponent =
    activitySubcomponent.loginSubcomponent().create()

  override fun provideLoginWebSubcomponent(): LoginWebSubcomponent =
    activitySubcomponent.loginWebSubcomponent().create()

  override fun provideAnilistSubcomponent(): AnilistSubcomponent =
    activitySubcomponent.anilistSubcomponent().create()
}