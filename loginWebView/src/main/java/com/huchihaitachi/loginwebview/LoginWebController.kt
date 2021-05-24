package com.huchihaitachi.loginwebview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import com.huchihaitachi.loginwebview.databinding.ControllerWebLoginBinding
import com.huchihaitachi.loginwebview.di.LoginWebSubcomponentProvider
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class LoginWebController() : Controller(), LoginWebView {
  @Inject lateinit var loginWebClient: LoginWebClient
  private var _binding: ControllerWebLoginBinding? = null
  private val binding: ControllerWebLoginBinding
    get() = _binding!!

  private val _loginCodeIntent = PublishSubject.create<String>()
  override val loginCodeIntent: Observable<String>
    get() = _loginCodeIntent

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup,
    savedViewState: Bundle?
  ): View {
    (activity as LoginWebSubcomponentProvider).provideLoginWebSubcomponent()
      .inject(this)
    loginWebClient.onLoggedIn = { code ->
      _loginCodeIntent.onNext(code)
      _loginCodeIntent.onComplete()
    }
    _binding = ControllerWebLoginBinding.inflate(inflater, container, false)
    binding.loginWv.apply {
      settings.javaScriptEnabled = true
      settings.domStorageEnabled = true
      settings.builtInZoomControls = true
      webViewClient = loginWebClient
      binding.loginWv.loadUrl(BuildConfig.AUTH_REDIRECT_URL)
    }
    return binding.root
  }

  override fun onAttach(view: View) {
    super.onAttach(view)
  }

  override fun onDestroyView(view: View) {
    _binding = null
    super.onDestroyView(view)
  }

  override fun render(state: LoginWebViewState) {

  }
}