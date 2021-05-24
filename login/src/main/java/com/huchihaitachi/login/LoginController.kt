package com.huchihaitachi.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import com.google.android.material.snackbar.Snackbar
import com.huchihaitachi.base.OAUTH
import com.huchihaitachi.base.visible
import com.huchihaitachi.login.databinding.ControllerLoginBinding
import com.huchihaitachi.login.di.LoginSubcomponentProvider
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Named

class LoginController(args: Bundle?) : Controller(args), LoginView {
  @Inject lateinit var presenter: LoginPresenter
  @Inject @Named(OAUTH) lateinit var oauth: () -> Unit

  private var _binding: ControllerLoginBinding? = null
  private val binding: ControllerLoginBinding
    get() = _binding!!
  private var snackbar: Snackbar? = null

  private val _loginIntent: PublishSubject<Unit> = PublishSubject.create()
  override val loginIntent: Observable<Unit>
    get() = _loginIntent

  constructor() : this(null)

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup,
    savedViewState: Bundle?
  ): View {
    (activity as LoginSubcomponentProvider).provideLoginSubcomponent()
      .inject(this)
    presenter.bind(this)
    _binding = ControllerLoginBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onAttach(view: View) {
    super.onAttach(view)
    binding.signInB.setOnClickListener {
      _loginIntent.onNext(Unit)
      _loginIntent.onComplete()
    }
    presenter.bindIntents()
  }

  override fun onDestroyView(view: View) {
    presenter.unbind()
    _binding = null
    super.onDestroyView(view)
  }

  override fun render(state: LoginViewState) {
    when (state) {
      is LoginViewState.ShowLogin -> {
        binding.loginPb.visible = false
        binding.progressBackgroundV.visible = false
        snackbar?.dismiss()
      }
      is LoginViewState.Loading -> {
        binding.loginPb.visible = true
        binding.progressBackgroundV.visible = true
        snackbar?.dismiss()
      }
      is LoginViewState.Success -> {
        binding.loginPb.visible = false
        binding.progressBackgroundV.visible = false
        snackbar?.dismiss()
      }
      is LoginViewState.Error -> {
        binding.loginPb.visible = false
        binding.progressBackgroundV.visible = false
        snackbar = Snackbar.make(
          binding.root,
          state.error.message ?: "Undefined error",
          Snackbar.LENGTH_INDEFINITE
        )
        snackbar?.show()
      }
    }
  }
}