package com.huchihaitachi.login.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import com.google.android.material.snackbar.Snackbar
import com.huchihaitachi.login.databinding.ControllerLoginBinding
import com.huchihaitachi.login.di.LoginSubcomponentProvider
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class LoginController(args: Bundle?) : Controller(args), LoginView {
  @Inject lateinit var presenter: LoginPresenter

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
    presenter.bindIntents()
    _binding = ControllerLoginBinding.inflate(inflater, container, false)
    binding.signInB.setOnClickListener {
      _loginIntent.onNext(Unit)
      _loginIntent.onComplete()
    }
    return binding.root
  }

  override fun onAttach(view: View) {
    super.onAttach(view)
  }

  override fun onDestroyView(view: View) {
    presenter.unbind()
    _binding = null
    super.onDestroyView(view)
  }

  override fun render(state: LoginViewState) {
  }
}